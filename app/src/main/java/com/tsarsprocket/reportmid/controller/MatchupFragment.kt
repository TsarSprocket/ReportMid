package com.tsarsprocket.reportmid.controller

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.tsarsprocket.reportmid.*
import com.tsarsprocket.reportmid.databinding.FragmentMatchupBinding
import com.tsarsprocket.reportmid.model.PuuidAndRegion
import com.tsarsprocket.reportmid.model.SideModel
import com.tsarsprocket.reportmid.presentation.PlayerPresentation
import com.tsarsprocket.reportmid.tools.formatPoints
import com.tsarsprocket.reportmid.viewmodel.MainActivityViewModel
import com.tsarsprocket.reportmid.viewmodel.MatchupViewModel
import kotlinx.android.synthetic.main.card_player_preview.view.*
import kotlinx.android.synthetic.main.fragment_matchup.view.*
import javax.inject.Inject
import kotlin.math.roundToInt

class MatchupFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MatchupViewModel> { viewModelFactory }

    private val activityViewModel by activityViewModels<MainActivityViewModel> { viewModelFactory }

    private lateinit var binding: FragmentMatchupBinding

    override fun onAttach( context: Context ) {
        ( context.applicationContext as ReportMidApp).comp.inject( this )
        super.onAttach( context )
        reloadMatch(false)
    }

    private fun reloadMatch(forceReload: Boolean) {
        requireArguments().let {
            if (it.getBoolean(ARG_RELOAD) || forceReload) {
                viewModel.puuidAndRegion = it.getParcelable(ARG_PUUID_AND_REG) ?: throw RuntimeException("Fragment ${this.javaClass.kotlin.simpleName} requires $ARG_PUUID_AND_REG argument")
                viewModel.loadForSummoner(viewModel.puuidAndRegion)
                it.putBoolean(ARG_RELOAD, false)
            }
        }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = DataBindingUtil.inflate( inflater, R.layout.fragment_matchup, container, false )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.matchInProgress.observe(viewLifecycleOwner ) { isInProgress ->
            binding.matchGroup.visibility = if( isInProgress ) View.VISIBLE else View.GONE
            binding.matchNotInProgressGroup.visibility = if( !isInProgress ) View.VISIBLE else View.GONE
        }

        viewModel.blueTeamParticipants.observe(viewLifecycleOwner ) { if( it != null ) populateTeam( binding.blueTeam, it, SideModel.BLUE ) }
        viewModel.redTeamParticipants.observe( viewLifecycleOwner ) { if( it != null ) populateTeam( binding.redTeam, it, SideModel.RED ) }

        with( binding.root.bottomNavigation ) {
            setOnNavigationItemSelectedListener{ menuItem -> navigateToSibling( menuItem ) }
            selectedItemId = R.id.matchupFragment
        }

        activityViewModel.selectedMenuItem.observe( viewLifecycleOwner ){ menuItemId ->
            when (menuItemId) {
                R.id.miMatchupRefresh -> reloadMatch(true)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.summoner.observe( viewLifecycleOwner ) {
            baseActivity.toolbar.title = getString( R.string.fragment_matchup_title_template ).format( it.name )
        }
    }

    private fun populateTeam(teamLayout: LinearLayout, playerPresentations: List<PlayerPresentation>, side: SideModel ) {
        teamLayout.removeAllViews()
        playerPresentations.forEach { playerPresentation ->
            val card = layoutInflater.inflate(R.layout.card_player_preview, teamLayout, false ) as CardView

            card.txtChampionSkill.text = getString( R.string.fragment_matchup_tmp_calculating )

            playerPresentation.championIconLive.observe( viewLifecycleOwner ) { card.imgChampionIcon.setImageBitmap( it ) }
            playerPresentation.summonerChampionSkillLive.observe( viewLifecycleOwner ) { card.txtChampionSkill.text = if( it >= 0 ) formatPoints( it ) else "n/a" }
            playerPresentation.summonerNameLive.observe( viewLifecycleOwner ) { card.txtSummonerName.text = it }
            playerPresentation.summonerLevelLive.observe( viewLifecycleOwner ) { card.txtSummonerLevel.text = it.toString() }
            playerPresentation.soloqueueRankLive.observe( viewLifecycleOwner ) { card.txtSummonerSoloQueueRank.text = it }
            playerPresentation.soloqueueWinrateLive.observe( viewLifecycleOwner ) { card.txtSummonerSoloQueueWinRate.text = ( ( it * 10 ).roundToInt() / 10f ).toString() }
            playerPresentation.summonerSpellDLive.observe( viewLifecycleOwner ) { card.imgSummonerSpellD.setImageBitmap( it ) }
            playerPresentation.summonerSpellFLive.observe( viewLifecycleOwner ) { card.imgSummonerSpellF.setImageBitmap( it ) }
            playerPresentation.primaryRunePathIconResIdLive.observe( viewLifecycleOwner ) { card.imgPrimaryRunePath.setImageResource( it ) }
            playerPresentation.secondaryRunePathIconResIdLive.observe( viewLifecycleOwner ) { card.imgSecondaryRunePath.setImageResource( it ) }

            card.setCardBackgroundColor( resources.getColor( if( side == SideModel.BLUE ) R.color.blueTeamBG else R.color.redTeamBG) )

            teamLayout.addView( card )
        }
    }

    fun navigateToSibling( item: MenuItem): Boolean {
        val navOptions = NavOptions.Builder().setLaunchSingleTop( true ).setPopUpTo(R.id.matchupFragment, true ).build()
        return when( item.itemId ) {
            R.id.profileOverviewFragment -> {
                val action = MatchupFragmentDirections.actionMatchupFragmentToProfileOverviewFragment( viewModel.puuidAndRegion )
                findNavController().navigate( action, navOptions )
                true
            }
            R.id.matchupFragment -> true
            R.id.matchHistoryFragment -> {
                val action = MatchupFragmentDirections.actionMatchupFragmentToMatchHistoryFragment( viewModel.puuidAndRegion )
                findNavController().navigate( action, navOptions )
                true
            }
            else -> false
        }
    }

    companion object {
        @JvmStatic
        fun newInstance( puuidAndRegion: PuuidAndRegion ) = MatchupFragment().apply {
            arguments = Bundle( 1 ).apply { putParcelable( ARG_PUUID_AND_REG, puuidAndRegion ) }
        }
    }
}

