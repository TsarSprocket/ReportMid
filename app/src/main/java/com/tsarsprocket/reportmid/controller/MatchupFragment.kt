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
                viewModel.puuid = it.getString(ARG_PUUID) ?: throw RuntimeException("Fragment ${this.javaClass.kotlin.simpleName} requires $ARG_PUUID argument")
                viewModel.loadForSummoner(puuid = viewModel.puuid)
                it.putBoolean(ARG_RELOAD, false)
            }
        }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = DataBindingUtil.inflate( inflater, R.layout.fragment_matchup, container, false )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.matchInProgress.observe( { lifecycle } ) { isInProgress ->
            binding.matchGroup.visibility = if( isInProgress ) View.VISIBLE else View.GONE
            binding.matchNotInProgressGroup.visibility = if( !isInProgress ) View.VISIBLE else View.GONE
        }

        viewModel.blueTeamParticipants.observe( { lifecycle } ) { if( it != null ) populateTeam( binding.blueTeam, it, SideModel.BLUE ) }
        viewModel.redTeamParticipants.observe( { lifecycle } ) { if( it != null ) populateTeam( binding.redTeam, it, SideModel.RED ) }

        with( binding.root.bottomNavigation ) {
            setOnNavigationItemSelectedListener{ menuItem -> navigateToSibling( menuItem ) }
            selectedItemId = R.id.matchupFragment
        }

        activityViewModel.selectedMenuItem.observe( { lifecycle } ){ menuItemId ->
            when (menuItemId) {
                R.id.miMatchupRefresh -> reloadMatch(true)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.summoner.observe( { lifecycle } ) {
            baseActivity.toolbar.title = getString( R.string.fragment_matchup_title_template ).format( it.name )
        }
    }

    private fun populateTeam(teamLayout: LinearLayout, playerPresentations: List<PlayerPresentation>, side: SideModel ) {
        teamLayout.removeAllViews()
        playerPresentations.forEach { playerPresentation ->
            val card = layoutInflater.inflate(R.layout.card_player_preview, teamLayout, false ) as CardView

            card.txtChampionSkill.text = getString( R.string.fragment_matchup_tmp_calculating )

            playerPresentation.championIconLive.observe( { lifecycle } ) { card.imgChampionIcon.setImageBitmap( it ) }
            playerPresentation.summonerChampionSkillLive.observe( { lifecycle } ) { card.txtChampionSkill.text = if( it >= 0 ) formatPoints( it ) else "n/a" }
            playerPresentation.summonerNameLive.observe( { lifecycle } ) { card.txtSummonerName.text = it }
            playerPresentation.summonerLevelLive.observe( { lifecycle } ) { card.txtSummonerLevel.text = it.toString() }
            playerPresentation.soloqueueRankLive.observe( { lifecycle } ) { card.txtSummonerSoloQueueRank.text = it }
            playerPresentation.soloqueueWinrateLive.observe( { lifecycle } ) { card.txtSummonerSoloQueueWinRate.text = ( ( it * 10 ).roundToInt() / 10f ).toString() }
            playerPresentation.summonerSpellDLive.observe( { lifecycle } ) { card.imgSummonerSpellD.setImageBitmap( it ) }
            playerPresentation.summonerSpellFLive.observe( { lifecycle } ) { card.imgSummonerSpellF.setImageBitmap( it ) }
            playerPresentation.primaryRunePathIconResIdLive.observe( { lifecycle } ) { card.imgPrimaryRunePath.setImageResource( it ) }
            playerPresentation.secondaryRunePathIconResIdLive.observe( { lifecycle } ) { card.imgSecondaryRunePath.setImageResource( it ) }

            card.setCardBackgroundColor( resources.getColor( if( side == SideModel.BLUE ) R.color.blueTeamBG else R.color.redTeamBG) )

            teamLayout.addView( card )
        }
    }

    fun navigateToSibling( item: MenuItem): Boolean {
        val navOptions = NavOptions.Builder().setLaunchSingleTop( true ).setPopUpTo(R.id.matchupFragment, true ).build()
        return when( item.itemId ) {
            R.id.profileOverviewFragment -> {
                val action = MatchupFragmentDirections.actionMatchupFragmentToProfileOverviewFragment( viewModel.puuid )
                findNavController().navigate( action, navOptions )
                true
            }
            R.id.matchupFragment -> true
            R.id.matchHistoryFragment -> {
                val action = MatchupFragmentDirections.actionMatchupFragmentToMatchHistoryFragment( viewModel.puuid )
                findNavController().navigate( action, navOptions )
                true
            }
            else -> false
        }
    }

    companion object {
        @JvmStatic
        fun newInstance( puuid: String ) = MatchupFragment().apply {
            arguments = Bundle( 1 ).apply { putString( ARG_PUUID, puuid ) }
        }
    }
}

