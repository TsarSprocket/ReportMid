package com.tsarsprocket.reportmid.controller

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tsarsprocket.reportmid.*
import com.tsarsprocket.reportmid.databinding.FragmentMatchHistoryBinding
import com.tsarsprocket.reportmid.model.PuuidAndRegion
import com.tsarsprocket.reportmid.model.SummonerModel
import com.tsarsprocket.reportmid.presentation.MatchResultPreviewData
import com.tsarsprocket.reportmid.viewmodel.MatchHistoryViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_match_history.view.*
import javax.inject.Inject

class MatchHistoryFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MatchHistoryViewModel> { viewModelFactory }

    private lateinit var binding: FragmentMatchHistoryBinding

    override fun onAttach(context: Context) {
        (context.applicationContext as ReportMidApp).comp.inject(this)
        super.onAttach(context)
        if (viewModel.activeSummonerModel.value == null) {
            viewModel.initialize(requireArguments().getParcelable(ARG_PUUID_AND_REG) ?: throw IllegalArgumentException("Missing ARG_PUUID_AND_REG argument"))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_match_history, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        with(binding.matchHistoryView) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            val disposable = Observable.create<SummonerModel> { emitter ->
                viewModel.activeSummonerModel.observe(viewLifecycleOwner) { summonerModel ->
                    emitter.onNext(summonerModel)
                    emitter.onComplete()
                }
            }
                .switchMap { summonerModel -> summonerModel.matchHistory }
                .observeOn(AndroidSchedulers.mainThread()).subscribe { matchHistory ->
                    adapter = MatchHistoryAdapter(object : MatchHistoryAdapter.IHistoryDataProvider {
                        override fun getCount(): Int = matchHistory.size
                        override fun getMatchData(i: Int): Observable<MatchResultPreviewData> {
                            return viewModel.fetchMatchPreviewInfo(i)
                        }
                    })

                    binding.matchHistoryView.visibility = View.VISIBLE
                    binding.progressLoading.visibility = View.GONE
                }
            if (disposable != null) viewModel.allDisposables.add(disposable)
        }

        with(binding.root.bottomNavigation.menu) {
            for (i in 0 until size()) with(get(i)) { if (id != R.id.matchHistoryFragment) isEnabled = true }
        }
        with(binding.root.bottomNavigation) {
            setOnNavigationItemSelectedListener { menuItem -> navigateToSibling(menuItem) }
            selectedItemId = R.id.matchHistoryFragment
        }

        viewModel.activeSummonerModel.observe(viewLifecycleOwner) {
            baseActivity.toolbar.title = getString(R.string.fragment_match_history_title_template).format(it.name)
        }

        return binding.root
    }

    private fun navigateToSibling(item: MenuItem): Boolean {
        val navOptions = NavOptions.Builder().setLaunchSingleTop(true).setPopUpTo(R.id.matchHistoryFragment, true).build()
        return when (item.itemId) {
            R.id.profileOverviewFragment -> {
                val sum = viewModel.activeSummonerModel.value!!
                val action = MatchHistoryFragmentDirections.actionMatchHistoryFragmentToProfileOverviewFragment(PuuidAndRegion(sum.puuid,sum.region))
                findNavController().navigate(action, navOptions)
                true
            }
            R.id.matchupFragment -> {
                val sum = viewModel.activeSummonerModel.value!!
                val action = MatchHistoryFragmentDirections.actionMatchHistoryFragmentToMatchupFragment(PuuidAndRegion(sum.puuid,sum.region))
                findNavController().navigate(action, navOptions)
                true
            }
            R.id.matchHistoryFragment -> true
            else -> false
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(puuidAndRegion: PuuidAndRegion) = MatchHistoryFragment().apply {
            arguments = Bundle(1).apply { putParcelable(ARG_PUUID_AND_REG, puuidAndRegion) }
        }
    }
}

