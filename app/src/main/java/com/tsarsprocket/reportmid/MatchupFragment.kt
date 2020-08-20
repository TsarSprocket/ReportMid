package com.tsarsprocket.reportmid

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
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
import com.tsarsprocket.reportmid.databinding.FragmentMatchupBinding
import com.tsarsprocket.reportmid.presentation.PlayerPresentation
import javax.inject.Inject

private const val ARG_PUUID = "com.tsarsprocket.reportmid.MatchupFragmentKt.ARG_PUUID"

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

        return binding.root
    }

    private fun populateTeam( teamLayout: LinearLayout, playerPresentations: List<PlayerPresentation> ) {
        teamLayout.removeAllViews()
        playerPresentations.forEach { playerPresentation ->
            val card = layoutInflater.inflate( R.layout.card_player_preview, teamLayout, true ) as CardView
            ( card[ R.id.imgChampionIcon ] as ImageView ).setImageBitmap( playerPresentation.championIcon )
            ( card[ R.id.txtChampionSkill ] as TextView ).text = playerPresentation.summonerChampionSkill.toString()
            ( card[ R.id.txtSummonerName ] as TextView ).text = playerPresentation.summonerName
            ( card[ R.id.txtSummonerLevel ] as TextView ).text = playerPresentation.summonerLevel.toString()
            ( card[ R.id.txtSummonerSoloQueueRank ] as TextView ).text = playerPresentation.soloqueueRank
            ( card[ R.id.txtSummonerSoloQueueWinRate ] as TextView ).text = ( Math.round( playerPresentation.soloqueueWinrate * 10f ) / 10f ).toString()
            ( card[ R.id.imgSummonerSpellD ] as ImageView ).setImageBitmap( playerPresentation.summonerSpellD )
            ( card[ R.id.imgSummonerSpellF ] as ImageView ).setImageBitmap( playerPresentation.summonerSpellF )
            ( card[ R.id.imgPrimaryRunePath ] as ImageView ).setImageResource( playerPresentation.primaryRunePathIconResId )
            ( card[ R.id.imgSecondaryRunePath ] as ImageView ).setImageResource( playerPresentation.secondaryRunePathIconResIs )
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

