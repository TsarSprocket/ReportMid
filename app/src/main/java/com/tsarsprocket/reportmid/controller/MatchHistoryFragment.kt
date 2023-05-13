package com.tsarsprocket.reportmid.controller

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.tsarsprocket.reportmid.ARG_PUUID_AND_REG
import com.tsarsprocket.reportmid.BaseFragment
import com.tsarsprocket.reportmid.R
import com.tsarsprocket.reportmid.ReportMidApp
import com.tsarsprocket.reportmid.base.viewmodel.ViewModelFactory
import com.tsarsprocket.reportmid.databinding.CardMatchHistoryBinding
import com.tsarsprocket.reportmid.databinding.FragmentMatchHistoryBinding
import com.tsarsprocket.reportmid.model.MatchHistoryModel
import com.tsarsprocket.reportmid.model.PuuidAndRegion
import com.tsarsprocket.reportmid.viewmodel.MatchHistoryViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MatchHistoryFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<MatchHistoryViewModel> { viewModelFactory }

    private lateinit var binding: FragmentMatchHistoryBinding

    private val disposer = CompositeDisposable()

    private val matchHistoryAdapter = MatchHistoryPagingAdapter(MatchComparator())

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
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_match_history, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        with(binding.matchHistoryView) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = matchHistoryAdapter
        }

        with(binding.bottomNavigation.menu) {
            for (i in 0 until size()) with(get(i)) { if (id != R.id.matchHistoryFragment) isEnabled = true }
        }
        with(binding.bottomNavigation) {
            setOnNavigationItemSelectedListener { menuItem -> navigateToSibling(menuItem) }
            selectedItemId = R.id.matchHistoryFragment
        }

        viewModel.activeSummonerModel.observe(viewLifecycleOwner) {
            baseActivity.toolbar.title = getString(R.string.fragment_match_history_title_template).format(it.name)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        disposer.clear()
        disposer.add(
            viewModel.flowableMatches
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    matchHistoryAdapter.submitData( viewLifecycleOwner.lifecycle, it )
                    binding.matchHistoryView.visibility = View.VISIBLE
                    binding.progressLoading.visibility = View.GONE
                })
    }

    override fun onStop() {
        disposer.clear()

        binding.matchHistoryView.visibility = View.GONE
        binding.progressLoading.visibility = View.VISIBLE

        super.onStop()
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

    class MatchHistoryPagingAdapter(diffCallback: DiffUtil.ItemCallback<MatchHistoryModel.MyMatch>)
            : PagingDataAdapter<MatchHistoryModel.MyMatch,CardViewHolderWithDisposer>(diffCallback) {

        override fun onBindViewHolder(holder: CardViewHolderWithDisposer, position: Int) {
            holder.disposer.clear()

            getItem(position)?.also { (summoner,gameId,matchSingle) ->
                matchSingle
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { match ->
                        with(holder) {
                            with( CardMatchHistoryBinding.bind(cardView) ) {
                                val itemViews = arrayOf(imageItem0, imageItem1, imageItem2, imageItem3, imageItem4, imageItem5, imageWard)

                                colourStripe.visibility = View.INVISIBLE
                                imgChampionIcon.setImageResource(R.drawable.champion_icon_placegolder)
                                iconPrimaryRune.setImageResource(R.drawable.item_icon_placegolder)
                                iconSecondaryRunePath.setImageResource(R.drawable.item_icon_placegolder)
                                iconSummonerSpellD.setImageResource(R.drawable.item_icon_placegolder)
                                iconSummonerSpellF.setImageResource(R.drawable.item_icon_placegolder)
                                txtGameMode.text = "???"
                                txtMainKDA.text = "?/?/?"
                                txtCS.text = "CS: ?"
                                itemViews.forEach { it.setImageResource(R.drawable.item_icon_placegolder) }

                                match.blueTeam.participants.plus(match.redTeam.participants)
                                    .find { it.puuid == summoner.puuid }
                                    ?.let { myParticipant ->
                                        colourStripe.setBackgroundColor(cardView.resources.getColor(
                                            if( match.remake ) R.color.bgRemake else if( myParticipant.isWinner ) R.color.bgWin else R.color.bgDefeat))
                                        colourStripe.visibility = View.VISIBLE

                                        with( holder ) {
                                            disposer.addAll(
                                                myParticipant.champion.icon.observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe { drawable -> imgChampionIcon.setImageDrawable(drawable) },
                                                myParticipant.summonerSpellD.icon.observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe { drawable -> iconSummonerSpellD.setImageDrawable(drawable) },
                                                myParticipant.summonerSpellF.icon.observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe { drawable -> iconSummonerSpellF.setImageDrawable(drawable) },
                                            )
                                            myParticipant.primaryRune?.icon?.observeOn(AndroidSchedulers.mainThread())
                                                ?.subscribe { drawable -> iconPrimaryRune.setImageDrawable(drawable) }?.let { disposer.add(it) }
                                            myParticipant.secondaryRunePath?.icon?.observeOn(AndroidSchedulers.mainThread())
                                                ?.subscribe { drawable -> iconSecondaryRunePath.setImageDrawable(drawable) }?.let { disposer.addAll() }
                                        }

                                        txtGameMode.text = cardView.resources.getString(match.gameType.titleResId)
                                        txtMainKDA.text = "${myParticipant.kills}/${myParticipant.deaths}/${myParticipant.assists}"
                                        txtCS.text = "CS: ${myParticipant.creepScore}"

                                        myParticipant.items.zip(itemViews).forEach { (item, imageView) ->
                                            if (item != null) {
                                                holder.disposer.add(item.icon.observeOn(AndroidSchedulers.mainThread()).subscribe { drawable -> imageView.setImageDrawable(drawable) })
                                            } else {
                                                imageView.setImageResource(R.drawable.item_empty)
                                            }
                                        }
                                    }
                            }
                        }
                    }
                    .also { holder.disposer.add(it) }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolderWithDisposer = CardViewHolderWithDisposer(
            LayoutInflater.from(parent.context).inflate(R.layout.card_match_history, parent, false ) as CardView
        )
    }

    class MatchComparator: DiffUtil.ItemCallback<MatchHistoryModel.MyMatch>() {
        override fun areItemsTheSame(oldItem: MatchHistoryModel.MyMatch, newItem: MatchHistoryModel.MyMatch): Boolean =
            oldItem.matchId == newItem.matchId && oldItem.summoner.id == newItem.summoner.id

        override fun areContentsTheSame(oldItem: MatchHistoryModel.MyMatch, newItem: MatchHistoryModel.MyMatch): Boolean =
            oldItem.matchId == newItem.matchId && oldItem.summoner.id == newItem.summoner.id // Sufficient assumption: same IDs always represent same data sets
    }

    companion object {
        @JvmStatic
        fun newInstance(puuidAndRegion: PuuidAndRegion) = MatchHistoryFragment().apply {
            arguments = Bundle(1).apply { putParcelable(ARG_PUUID_AND_REG, puuidAndRegion) }
        }
    }
}

