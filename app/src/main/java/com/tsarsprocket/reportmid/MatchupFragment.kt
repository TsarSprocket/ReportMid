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
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.tsarsprocket.reportmid.databinding.FragmentMatchupBinding
import com.tsarsprocket.reportmid.presentation.PlayerPresentation
import kotlinx.android.synthetic.main.fragment_landing.view.*
import javax.inject.Inject

class MatchupFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MatchupViewModel> { viewModelFactory }

    private lateinit var binding: FragmentMatchupBinding

    override fun onAttach( context: Context ) {
        ( context.applicationContext as ReportMidApp ).comp.inject( this )
        super.onAttach( context )
        arguments?.let { viewModel.loadForSummoner( puuid = it.getString( ARG_PUUID )!! )  }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = DataBindingUtil.inflate( inflater, R.layout.fragment_matchup, container, false )
        binding.viewModel = viewModel

        viewModel.blueTeamParticipants.observe( { lifecycle } ) { if( it != null ) populateTeam( binding.blueTeam, it ) }
        viewModel.redTeamParticipants.observe( { lifecycle } ) { if( it != null ) populateTeam( binding.redTeam, it ) }

        with( binding.root.bottomNavigation ) {
            setOnNavigationItemSelectedListener{ menuItem -> navigateToSibling( menuItem ) }
            selectedItemId = R.id.matchupFragment
        }

        return binding.root
    }

    private fun populateTeam( teamLayout: LinearLayout, playerPresentations: List<PlayerPresentation> ) {
        teamLayout.removeAllViews()
        playerPresentations.forEach { playerPresentation ->
            val card = layoutInflater.inflate( R.layout.card_player_preview, teamLayout, false ) as CardView
            card.findViewById<ImageView>( R.id.imgChampionIcon ).setImageBitmap( playerPresentation.championIcon )
            card.findViewById<TextView>( R.id.txtChampionSkill ).text = playerPresentation.summonerChampionSkill.toString()
            card.findViewById<TextView>( R.id.txtSummonerName ).text = playerPresentation.summonerName
            card.findViewById<TextView>( R.id.txtSummonerLevel ).text = playerPresentation.summonerLevel.toString()
            card.findViewById<TextView>( R.id.txtSummonerSoloQueueRank ).text = playerPresentation.soloqueueRank
            card.findViewById<TextView>( R.id.txtSummonerSoloQueueWinRate ).text = ( Math.round( playerPresentation.soloqueueWinrate * 10f ) / 10f ).toString()
            card.findViewById<ImageView>( R.id.imgSummonerSpellD ).setImageBitmap( playerPresentation.summonerSpellD )
            card.findViewById<ImageView>( R.id.imgSummonerSpellF ).setImageBitmap( playerPresentation.summonerSpellF )
            card.findViewById<ImageView>( R.id.imgPrimaryRunePath ).setImageResource( playerPresentation.primaryRunePathIconResId )
            card.findViewById<ImageView>( R.id.imgSecondaryRunePath ).setImageResource( playerPresentation.secondaryRunePathIconResIs )
            teamLayout.addView( card )
        }
    }

    fun navigateToSibling( item: MenuItem): Boolean {
        val navOptions = NavOptions.Builder().setLaunchSingleTop( true ).setPopUpTo( R.id.matchupFragment, true ).build()
        return when( item.itemId ) {
            R.id.landingFragment -> {
                val action = MatchupFragmentDirections.actionMatchupFragmentToLandingFragment()
                findNavController().navigate( action, navOptions )
                true
            }
            R.id.matchupFragment -> true
            R.id.matchHistoryFragment -> {
                val action = MatchupFragmentDirections.actionMatchupFragmentToMatchHistoryFragment()
                findNavController().navigate( action, navOptions )
                true
            }
            else -> false
        }
    }

    companion object {
        @JvmStatic
        fun newInstance( puuid: String ): MatchupFragment {
            val fragment = MatchupFragment()

            val args = Bundle( 1 )
            args.putString( ARG_PUUID, puuid )

            fragment.arguments = args

            return fragment
        }
    }
}

