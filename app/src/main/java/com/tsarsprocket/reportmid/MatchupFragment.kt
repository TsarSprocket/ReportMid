package com.tsarsprocket.reportmid

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.tsarsprocket.reportmid.databinding.FragmentMatchupBinding
import com.tsarsprocket.reportmid.model.SideModel
import com.tsarsprocket.reportmid.presentation.PlayerPresentation
import kotlinx.android.synthetic.main.fragment_matchup.view.*
import javax.inject.Inject
import kotlin.math.roundToInt

class MatchupFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MatchupViewModel> { viewModelFactory }

    private lateinit var binding: FragmentMatchupBinding

    override fun onAttach( context: Context ) {
        ( context.applicationContext as ReportMidApp ).comp.inject( this )
        super.onAttach( context )
        if( viewModel.summoner == null ) requireArguments().let {
            viewModel.puuid = it.getString( ARG_PUUID )
            viewModel.loadForSummoner( puuid = viewModel.puuid!! )
        }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = DataBindingUtil.inflate( inflater, R.layout.fragment_matchup, container, false )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.blueTeamParticipants.observe( { lifecycle } ) { if( it != null ) populateTeam( binding.blueTeam, it, SideModel.BLUE ) }
        viewModel.redTeamParticipants.observe( { lifecycle } ) { if( it != null ) populateTeam( binding.redTeam, it, SideModel.RED ) }

        with( binding.root.bottomNavigation ) {
            setOnNavigationItemSelectedListener{ menuItem -> navigateToSibling( menuItem ) }
            selectedItemId = R.id.matchupFragment
        }

        return binding.root
    }

    private fun populateTeam( teamLayout: LinearLayout, playerPresentations: List<PlayerPresentation>, side: SideModel ) {
        teamLayout.removeAllViews()
        playerPresentations.forEach { playerPresentation ->
            val card = layoutInflater.inflate( R.layout.card_player_preview, teamLayout, false ) as CardView

            playerPresentation.championIconLive.observe( { lifecycle } ) { card.findViewById<ImageView>( R.id.imgChampionIcon ).setImageBitmap( it ) }
            playerPresentation.summonerChampionSkillLive.observe( { lifecycle } ) { card.findViewById<TextView>( R.id.txtChampionSkill ).text = if( it >= 0 ) it.toString() else "n/a" }
            playerPresentation.summonerNameLive.observe( { lifecycle } ) { card.findViewById<TextView>( R.id.txtSummonerName ).text = it }
            playerPresentation.summonerLevelLive.observe( { lifecycle } ) { card.findViewById<TextView>( R.id.txtSummonerLevel ).text = it.toString() }
            playerPresentation.soloqueueRankLive.observe( { lifecycle } ) { card.findViewById<TextView>( R.id.txtSummonerSoloQueueRank ).text = it }
            playerPresentation.soloqueueWinrateLive.observe( { lifecycle } ) { card.findViewById<TextView>( R.id.txtSummonerSoloQueueWinRate ).text = ( ( it * 10 ).roundToInt() / 10f ).toString() }
            playerPresentation.summonerSpellDLive.observe( { lifecycle } ) { card.findViewById<ImageView>( R.id.imgSummonerSpellD ).setImageBitmap( it ) }
            playerPresentation.summonerSpellFLive.observe( { lifecycle } ) { card.findViewById<ImageView>( R.id.imgSummonerSpellF ).setImageBitmap( it ) }
            playerPresentation.primaryRunePathIconResIdLive.observe( { lifecycle } ) { card.findViewById<ImageView>( R.id.imgPrimaryRunePath ).setImageResource( it ) }
            playerPresentation.secondaryRunePathIconResIdLive.observe( { lifecycle } ) { card.findViewById<ImageView>( R.id.imgSecondaryRunePath ).setImageResource( it ) }

            card.setCardBackgroundColor( resources.getColor( if( side == SideModel.BLUE ) R.color.blueTeamBG else R.color.redTeamBG ) )

            teamLayout.addView( card )
        }
    }

    fun navigateToSibling( item: MenuItem): Boolean {
        val navOptions = NavOptions.Builder().setLaunchSingleTop( true ).setPopUpTo( R.id.matchupFragment, true ).build()
        return when( item.itemId ) {
            R.id.profileOverviewFragment -> {
                val puuid = viewModel.puuid
                if( puuid != null ) {
                    val action = MatchupFragmentDirections.actionMatchupFragmentToProfileOverviewFragment( puuid )
                    findNavController().navigate(action, navOptions)
                    true
                } else { false }
            }
            R.id.matchupFragment -> true
            R.id.matchHistoryFragment -> {
                val puuid = viewModel.puuid
                if( puuid != null ) {
                    val action = MatchupFragmentDirections.actionMatchupFragmentToMatchHistoryFragment( puuid )
                    findNavController().navigate(action, navOptions)
                    true
                } else { false }
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

