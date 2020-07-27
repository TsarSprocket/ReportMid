package com.tsarsprocket.reportmid

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
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

val arrIconViewIds = arrayOf( R.id.imageItem0, R.id.imageItem1, R.id.imageItem2, R.id.imageItem3, R.id.imageItem4, R.id.imageItem5 )

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

                binding.matchHistoryView.visibility = View.VISIBLE
                binding.progressLoading.visibility = View.GONE
            }
            if( disposable != null ) viewModel.allDisposables.add( disposable )
        }

        requireActivity().findViewById<Toolbar>( R.id.toolbar ).title = getString( R.string.fragment_match_history_title_template ).format( viewModel.activeSummonerModel.value?.name )

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = MatchHistoryFragment()
    }
}

class MatchHistoryAdapter( val dataProvider: IHistoryDataProvider ): RecyclerView.Adapter<MatchHistoryViewHolder>() {

    var cardBGColour: ColorStateList? = null

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

            if( cardBGColour == null ) {
                cardBGColour = cardBackgroundColor
            } else {
                setCardBackgroundColor( cardBGColour )
            }

            findViewById<ImageView>( R.id.imgChampionIcon ).setImageResource( R.drawable.champion_icon_placegolder )

            findViewById<ImageView>( R.id.iconPrimaryRune ).visibility = View.INVISIBLE
            findViewById<ImageView>( R.id.iconSecondaryRunePath ).visibility = View.INVISIBLE

            findViewById<TextView>( R.id.txtGameMode ).text = ""
            findViewById<TextView>( R.id.txtMainKDA ).text = "?/?/?"
            findViewById<TextView>( R.id.txtCS ).text = "CS: ?"

            findViewById<ImageView>( R.id.iconSummonerSpellD ).visibility = View.INVISIBLE
            findViewById<ImageView>( R.id.iconSummonerSpellF ).visibility = View.INVISIBLE

            arrIconViewIds.forEach { findViewById<ImageView>( it ).visibility = View.INVISIBLE }

            findViewById<ImageView>( R.id.imageWard ).visibility = View.INVISIBLE
        }

        holder.allDisposables.add( dataProvider.getMatchData( position )
            .observeOn( AndroidSchedulers.mainThread() )
            .subscribe { data ->
                with( holder.cardView ) {
                    this.setCardBackgroundColor( resources.getColor( if( data.remake ) R.color.colorBGRemake else if( data.hasWon ) R.color.colorBGWin else R.color.colorBGDefeat ) )

                    findViewById<ImageView>( R.id.imgChampionIcon ).setImageBitmap( data.mainChampionBitmap )

                    with( findViewById<ImageView>( R.id.iconPrimaryRune ) ) { setImageResource( data.primaryRuneIconResId ); visibility = View.VISIBLE }
                    with( findViewById<ImageView>( R.id.iconSecondaryRunePath ) ){ setImageResource( data.secondaryRunePathIconResId ); visibility = View.VISIBLE }

                    findViewById<TextView>( R.id.txtGameMode ).text = resources.getString( data.gameModeNameResId )
                    findViewById<TextView>( R.id.txtMainKDA ).text = "${data.mainKills}/${data.mainDeaths}/${data.mainAssists}"
                    findViewById<TextView>( R.id.txtCS ).text = "CS: ${data.creepScore.toString()}"

                    with( findViewById<ImageView>( R.id.iconSummonerSpellD ) ){ setImageBitmap( data.bmSummonerSpellD ); visibility = View.VISIBLE }
                    with( findViewById<ImageView>( R.id.iconSummonerSpellF ) ){ setImageBitmap( data.bmSummonerSpellF ); visibility = View.VISIBLE }

                    val vlItems = List( arrIconViewIds.size ){ i -> findViewById<ImageView>( arrIconViewIds[ i ] ) }
                    val iconWard = findViewById<ImageView>( R.id.imageWard )

                    for( i in vlItems.indices ) {
                        val imageView = vlItems[ i ]
                        if( i < data.itemIcons.size - 1 ) {
                            imageView.setImageBitmap( data.itemIcons[ i ] )/*.also { visibility = View.VISIBLE }*/
                        } else {
                            imageView.setImageResource( R.drawable.item_empty ) // imageView.visibility = View.INVISIBLE
                        }
                        imageView.visibility = View.VISIBLE
                    }

                    if( data.itemIcons.isNotEmpty() ) {
                        iconWard.setImageBitmap( data.itemIcons.last() ).also { visibility = View.VISIBLE }
                    } else {
                        iconWard.setImageResource( R.drawable.item_empty ) // iconWard.visibility = View.INVISIBLE
                    }
                    iconWard.visibility = View.VISIBLE
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

