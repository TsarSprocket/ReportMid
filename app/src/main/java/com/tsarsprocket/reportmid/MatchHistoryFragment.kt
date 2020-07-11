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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tsarsprocket.reportmid.databinding.FragmentMatchHistoryBinding
import com.tsarsprocket.reportmid.presentation.MatchResultPreviewData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
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
            setHasFixedSize( true )
            layoutManager = LinearLayoutManager( context )
            val disposable = viewModel.activeSummonerModel.value?.matchHistory?.observeOn( AndroidSchedulers.mainThread() )?.subscribe { matchHistory ->
                adapter = MatchHistoryAdapter( object: MatchHistoryAdapter.IHistoryDataProvider {
                    override fun getCount(): Int = matchHistory.size
                    override fun getMatchData( i: Int ): Observable<MatchResultPreviewData> {
                        return viewModel.fetchMatchPreviewInfo( i )
                    }
                } )
            }
            if( disposable != null ) viewModel.allDisposables.add( disposable )
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = MatchHistoryFragment()
    }
}

class MatchHistoryAdapter( val dataProvider: IHistoryDataProvider ): RecyclerView.Adapter<MatchHistoryViewHolder>() {

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): MatchHistoryViewHolder {
        val cardView = LayoutInflater.from( parent.context )
            .inflate( R.layout.match_history_card, parent, false ) as CardView
        return MatchHistoryViewHolder( cardView )
    }

    override fun getItemCount(): Int = dataProvider.getCount()

    override fun onBindViewHolder( holder: MatchHistoryViewHolder, position: Int ) {

        holder.allDisposables.dispose()

        holder.allDisposables = CompositeDisposable()

        with( holder.cardView ) {
            findViewById<ImageView>( R.id.imgChampionIcon ).setImageResource( R.drawable.champion_icon_placegolder )
            findViewById<TextView>( R.id.txtGameOutcome ).text = ""
            findViewById<TextView>( R.id.txtTeamKDA ).text = "?/?/?"
            findViewById<TextView>( R.id.txtMainKDA ).text = "?/?/?"
            val teams = arrayListOf<ViewGroup>( findViewById<LinearLayout>(R.id.layoutBlueTeamIcons), findViewById<LinearLayout>(R.id.layoutRedTeamIcons) )
            for( vg in teams ) {
                for (i: Int in 0 until vg.childCount) {
                    val v = vg[i]
                    if (v is ImageView) v.setImageResource(R.drawable.champion_icon_placegolder_one_half)
                }
            }
        }

        holder.allDisposables.add( dataProvider.getMatchData( position )
            .observeOn( AndroidSchedulers.mainThread() )
            .subscribe { data ->
                with( holder.cardView ) {
                    findViewById<ImageView>( R.id.imgChampionIcon ).setImageBitmap( data.mainChampionBitmap )
                    findViewById<TextView>( R.id.txtGameOutcome ).text = if( data.hasWon ) context.getString( R.string.fragment_match_history_message_win ) else context.getString( R.string.fragment_match_history_message_defeat )
                    findViewById<TextView>( R.id.txtTeamKDA ).text = "${data.teamKills}/${data.teamDeaths}/${data.teamAssists}"
                    findViewById<TextView>( R.id.txtMainKDA ).text = "${data.mainKills}/${data.mainDeaths}/${data.mainAssists}"
                    val viewGroups = arrayOf( findViewById<LinearLayout>( R.id.layoutBlueTeamIcons ), findViewById( R.id.layoutRedTeamIcons ) )
                    for( j in viewGroups.indices ) {
                        for ( i in 0 until viewGroups[ j ].childCount ) {
                            val view = viewGroups[ j ][ i ] as ImageView
                            if ( i < data.teamsIcons[ j ].size ) {
                                view.setImageBitmap( data.teamsIcons[ j ][ i ] )
                                view.visibility = View.VISIBLE
                            } else {
                                view.setImageResource( R.drawable.champion_icon_placegolder_one_half )
                                view.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        )
    }

    interface IHistoryDataProvider {
        fun getCount(): Int
        fun getMatchData( i: Int ): Observable<MatchResultPreviewData>
    }
}

class MatchHistoryViewHolder( val cardView: CardView ): RecyclerView.ViewHolder( cardView ) {
    var allDisposables = CompositeDisposable()
}

