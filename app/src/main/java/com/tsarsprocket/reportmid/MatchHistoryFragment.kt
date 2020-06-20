package com.tsarsprocket.reportmid

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tsarsprocket.reportmid.databinding.FragmentMatchHistoryBinding
import com.tsarsprocket.reportmid.model.MatchHistoryModel
import com.tsarsprocket.reportmid.model.SummonerModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MatchHistoryFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by activityViewModels<LandingViewModel> { viewModelFactory }

    private lateinit var binding: FragmentMatchHistoryBinding

    override fun onAttach( context: Context ) {
        ( context.applicationContext as ReportMidApp ).comp.inject( this )
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate( inflater, R.layout.fragment_match_history, container, false )
        binding.viewModel = viewModel

        with( binding.matchHistoryView ) {

            val summoner = viewModel.activeSummonerModel.value!!

            setHasFixedSize( true )
            layoutManager = LinearLayoutManager( context )
            adapter = MatchHistoryAdapter( summoner, this@MatchHistoryFragment )
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = MatchHistoryFragment()
    }
}

class MatchHistoryAdapter( val summoner: SummonerModel, val lifecycleOwner: LifecycleOwner ): RecyclerView.Adapter<MatchHistoryViewHolder>() {

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): MatchHistoryViewHolder {
        val cardView = LayoutInflater.from( parent.context )
            .inflate( R.layout.match_history_card, parent, false ) as CardView
        return MatchHistoryViewHolder( cardView )
    }

    override fun getItemCount(): Int = summoner.matchHistory.size

    override fun onBindViewHolder( holder: MatchHistoryViewHolder, position: Int ) {

        lifecycleOwner.lifecycleScope.launch( Dispatchers.IO ) {

            val match = summoner.matchHistory.getMatch( position )

            val asParticipant = match.shadowMatch.blueTeam.participants
                .union( match.shadowMatch.redTeam.participants ).find { it.summoner.puuid == summoner.puuid }

            val bitmap = asParticipant!!.champion.image.get()
            val kda = with( asParticipant!!.stats ) { "${kills}/${deaths}/${assists}" }

            launch( Dispatchers.Main ) {

                with( holder.cardView ) {
                    findViewById<ImageView>( R.id.imgChampionIcon ).setImageBitmap( bitmap )
                    findViewById<TextView>( R.id.txtKDA ).text = kda
                }
            }
        }

    }
}

class MatchHistoryViewHolder( val cardView: CardView ): RecyclerView.ViewHolder( cardView )

