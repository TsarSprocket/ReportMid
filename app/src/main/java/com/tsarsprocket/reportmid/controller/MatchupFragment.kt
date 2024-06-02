package com.tsarsprocket.reportmid.controller

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tsarsprocket.reportmid.ARG_PUUID_AND_REG
import com.tsarsprocket.reportmid.ARG_RELOAD
import com.tsarsprocket.reportmid.BaseFragment
import com.tsarsprocket.reportmid.R
import com.tsarsprocket.reportmid.ReportMidApp
import com.tsarsprocket.reportmid.baseApi.viewmodel.ViewModelFactory
import com.tsarsprocket.reportmid.databinding.CardPlayerPreviewBinding
import com.tsarsprocket.reportmid.databinding.FragmentMatchupBinding
import com.tsarsprocket.reportmid.lol.model.PuuidAndRegion
import com.tsarsprocket.reportmid.model.SideModel
import com.tsarsprocket.reportmid.presentation.PlayerPresentation
import com.tsarsprocket.reportmid.summonerApi.model.Summoner
import com.tsarsprocket.reportmid.tools.formatPoints
import com.tsarsprocket.reportmid.viewmodel.MainActivityViewModel
import com.tsarsprocket.reportmid.viewmodel.MatchupViewModel
import javax.inject.Inject
import kotlin.math.roundToInt

class MatchupFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<MatchupViewModel> { viewModelFactory }

    private val activityViewModel by activityViewModels<MainActivityViewModel> { viewModelFactory }

    private lateinit var binding: FragmentMatchupBinding

    private val teamsAdapter = TeamsAdapter()

    //  Methods  //////////////////////////////////////////////////////////////

    override fun onAttach(context: Context) {
        (context.applicationContext as ReportMidApp).comp.inject(this)
        super.onAttach(context)
        reloadMatch(false)
    }

    private fun reloadMatch(forceReload: Boolean) {
        requireArguments().let {
            if (it.getBoolean(ARG_RELOAD) || forceReload) {
                viewModel.puuidAndRegion = it.getParcelable(ARG_PUUID_AND_REG)
                    ?: throw RuntimeException("Fragment ${this.javaClass.kotlin.simpleName} requires $ARG_PUUID_AND_REG argument")
                viewModel.loadForSummoner(viewModel.puuidAndRegion)
                it.putBoolean(ARG_RELOAD, false)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_matchup, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.matchInProgress.observe(viewLifecycleOwner) { isInProgress ->
            binding.matchGroup.visibility = if (isInProgress) View.VISIBLE else View.GONE
            binding.matchNotInProgressGroup.visibility = if (!isInProgress) View.VISIBLE else View.GONE
        }

        with (binding.teamsRecycleView) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = teamsAdapter
        }

        viewModel.blueTeamParticipants.observe(viewLifecycleOwner) { teamsAdapter.blueTeam = it }
        viewModel.redTeamParticipants.observe(viewLifecycleOwner) { teamsAdapter.redTeam = it }

        with(binding.bottomNavigation) {
            setOnNavigationItemSelectedListener { menuItem -> navigateToSibling(menuItem) }
            selectedItemId = R.id.matchupFragment
        }

        activityViewModel.selectedMenuItem.observe(viewLifecycleOwner) { menuItemId ->
            when (menuItemId) {
                R.id.miMatchupRefresh -> reloadMatch(true)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.summoner.observe(viewLifecycleOwner) {
            baseActivity.toolbar.title = getString(R.string.fragment_matchup_title_template).format(it.name)
        }
    }

    fun navigateToSibling(item: MenuItem): Boolean {
        val navOptions = NavOptions.Builder().setLaunchSingleTop(true).setPopUpTo(R.id.matchupFragment, true).build()
        return when (item.itemId) {
            R.id.profileOverviewFragment -> {
                val action = MatchupFragmentDirections.actionMatchupFragmentToProfileOverviewFragment(viewModel.puuidAndRegion)
                findNavController().navigate(action, navOptions)
                true
            }
            R.id.matchupFragment -> true
            R.id.matchHistoryFragment -> {
                val action = MatchupFragmentDirections.actionMatchupFragmentToMatchHistoryFragment(viewModel.puuidAndRegion)
                findNavController().navigate(action, navOptions)
                true
            }
            else -> false
        }
    }

    //  Static  ///////////////////////////////////////////////////////////////

    companion object {
        fun newInstance(puuidAndRegion: PuuidAndRegion) = MatchupFragment().apply {
            arguments = Bundle(1).apply { putParcelable(ARG_PUUID_AND_REG, puuidAndRegion) }
        }
    }

    //  Classes  //////////////////////////////////////////////////////////////

    class ColoredCardViewHolder(cardView: CardView, var color: Int) : CardViewHolderWithDisposer(cardView)

    inner class TeamsAdapter : RecyclerView.Adapter<ColoredCardViewHolder>() {

        var blueTeam: List<PlayerPresentation> = listOf()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        var redTeam: List<PlayerPresentation> = listOf()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColoredCardViewHolder {
            val card = LayoutInflater.from(parent.context).inflate(R.layout.card_player_preview, parent, false) as CardView
            return ColoredCardViewHolder(card,0)
        }

        override fun onBindViewHolder(holder: ColoredCardViewHolder, position: Int) {
            if (position !in 0 .. blueTeam.size + redTeam.size) return

            val side = if (position < blueTeam.size) SideModel.BLUE else SideModel.RED
            val item = if (side == SideModel.BLUE) blueTeam[position] else redTeam[position - blueTeam.size]
            val cardBinding = CardPlayerPreviewBinding.bind(holder.cardView)

            cardBinding.txtChampionSkill.text = getString(R.string.fragment_matchup_tmp_calculating)

            item.championIconLive.observe(viewLifecycleOwner) { cardBinding.imgChampionIcon.setImageDrawable(it) }
            item.summonerChampionSkillLive.observe(viewLifecycleOwner) { cardBinding.txtChampionSkill.text = if (it >= 0) formatPoints(it) else getString(R.string.fragment_matchup_skill_na) }
            item.summonerNameLive.observe(viewLifecycleOwner) { cardBinding.txtSummonerName.text = it }
            item.summonerLevelLive.observe(viewLifecycleOwner) { cardBinding.txtSummonerLevel.text = it.toString() }
            item.soloqueueRankLive.observe(viewLifecycleOwner) { cardBinding.txtSummonerSoloQueueRank.text = it }
            item.soloqueueWinrateLive.observe(viewLifecycleOwner) { cardBinding.txtSummonerSoloQueueWinRate.text = ((it * 10).roundToInt() / 10f).toString() }
            item.summonerSpellDLive.observe(viewLifecycleOwner) { cardBinding.imgSummonerSpellD.setImageDrawable(it) }
            item.summonerSpellFLive.observe(viewLifecycleOwner) { cardBinding.imgSummonerSpellF.setImageDrawable(it) }
            item.primaryRuneIconLive.observe(viewLifecycleOwner) { cardBinding.imgPrimaryRune.setImageDrawable(it) }
            item.secondaryRunePathIconLive.observe(viewLifecycleOwner) { cardBinding.imgSecondaryRunePath.setImageDrawable(it) }

            cardBinding.colourStripe.setBackgroundColor(resources.getColor(if (side == SideModel.BLUE) R.color.blueTeamBG else R.color.redTeamBG))

            cardBinding.root.setOnClickListener { item.summoner.observe(viewLifecycleOwner) { goToChampion( it ) } }
        }

        override fun getItemCount(): Int = blueTeam.size + redTeam.size
    }

    private fun goToChampion(summoner: Summoner) {
        val action = MatchupFragmentDirections.actionMatchupFragmentToProfileOverviewFragment(summoner.puuidAndRegion)
        findNavController().navigate(action)
    }

}

