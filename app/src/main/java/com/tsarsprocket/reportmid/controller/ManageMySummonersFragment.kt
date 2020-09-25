package com.tsarsprocket.reportmid.controller

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tsarsprocket.reportmid.*
import com.tsarsprocket.reportmid.databinding.FragmentManageMySummonersBinding
import com.tsarsprocket.reportmid.model.SummonerModel
import com.tsarsprocket.reportmid.tools.OneTimeObserver
import com.tsarsprocket.reportmid.tools.getNavigationReturnedValue
import com.tsarsprocket.reportmid.tools.removeNavigationReturnedValue
import com.tsarsprocket.reportmid.viewmodel.MainActivityViewModel
import com.tsarsprocket.reportmid.viewmodel.ManageMySummonersViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.card_my_managed_summoner.view.*
import javax.inject.Inject

class ManageMySummonersFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<ManageMySummonersViewModel> { viewModelFactory }
    private val activityViewModel by activityViewModels<MainActivityViewModel> { viewModelFactory }

    lateinit var binding: FragmentManageMySummonersBinding

    override fun onAttach(context: Context) {
        (context.applicationContext as ReportMidApp).comp.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_manage_my_summoners, container, false)
        binding.lifecycleOwner = this
        binding.handler = EventHandler()

        baseActivity.toolbar.title = getString(R.string.fragment_manage_my_summoners_title)

        val mySummonersAdapter = MySummonersAdapter()
        with(binding.recvMySummoners) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mySummonersAdapter
            viewModel.mySummonersLive.observe({ lifecycle }) { lstSummoners ->
                binding.progressLoading.visibility = View.GONE
                visibility = View.VISIBLE
                mySummonersAdapter.summoners = lstSummoners.toTypedArray()
                val iterator = viewModel.checkedSummoners.iterator()
                while (iterator.hasNext()) {
                    val s = iterator.next()
                    if (!lstSummoners.contains(s)) iterator.remove()
                }
            }
        }

        activityViewModel.selectedMenuItem.observe( viewLifecycleOwner ) {
            when(it) {
                R.id.miManageMyAccountsDelete -> { deleteSelected() }
            }
        }

        activityViewModel.menuRefreshed.observe( viewLifecycleOwner ) {
            baseActivity.toolbar.menu.findItem(R.id.miManageMyAccountsDelete).isEnabled = viewModel.checkedSummoners.isNotEmpty()
        }

        return binding.root
    }

    fun resetCheckedSummoners() {
        viewModel.checkedSummoners.clear()
        baseActivity.toolbar.menu.findItem(R.id.miManageMyAccountsDelete).isEnabled = false
    }

    fun checkSummoner(summoner: SummonerModel) {
        viewModel.checkedSummoners.add(summoner)
        baseActivity.toolbar.menu.findItem(R.id.miManageMyAccountsDelete).isEnabled = true
    }

    fun uncheckSummoner(summoner: SummonerModel) {
        viewModel.checkedSummoners.apply {
            remove(summoner)
            if (count() == 0) baseActivity.toolbar.menu.findItem(R.id.miManageMyAccountsDelete).isEnabled = false
        }
    }

    private fun deleteSelected() {
        TODO("Not yet implemented")
    }

    private fun doAddSummoner() {
        object : OneTimeObserver<String>() {
            override fun onOneTimeChanged(v:String) {
                removeNavigationReturnedValue<String>(RESULT_PUUID)
                viewModel.addMySummoner(v)
            }
        }.observeOn(getNavigationReturnedValue<String>(RESULT_PUUID),this)

        findNavController().navigate(ManageMySummonersFragmentDirections.actionManageMySummonersFragmentToAddSummonerGraph(null))
    }

    companion object {
        fun newInstance() = ManageMySummonersFragment()
    }

    //  Classes  //////////////////////////////////////////////////////////////

    inner class MySummonersAdapter : RecyclerView.Adapter<CardViewHolderWithDisposer>() {

        var summoners: Array<SummonerModel> = arrayOf()
            set(v: Array<SummonerModel>) {
                field = v
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolderWithDisposer =
            CardViewHolderWithDisposer(LayoutInflater.from(parent.context).inflate(R.layout.card_my_managed_summoner, parent, false) as CardView)

        override fun onBindViewHolder(holder: CardViewHolderWithDisposer, position: Int) {
            holder.disposer.clear()
            val summoner = summoners[position]
            with(holder.cardView) {
                cbSelected.isChecked = viewModel.checkedSummoners.contains(summoner)
                cbSelected.setOnCheckedChangeListener { _, isChecked -> if (isChecked) checkSummoner(summoner) else uncheckSummoner(summoner) }
                holder.disposer.add(summoner.icon.observeOn(AndroidSchedulers.mainThread()).subscribe { bmp -> imgProfileIcon.setImageBitmap(bmp) })
                txtSummonerName.text = summoner.name
                txtRegion.text = summoner.region.tag
            }
        }

        override fun getItemCount() = summoners.size
    }

    inner class EventHandler {
        fun onAddSummonerClick(view: View) {
            doAddSummoner()
        }
    }
}