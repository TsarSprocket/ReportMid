package com.tsarsprocket.reportmid

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tsarsprocket.reportmid.presentation.MatchResultPreviewData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class MatchHistoryAdapter( val dataProvider: IHistoryDataProvider ): RecyclerView.Adapter<MatchHistoryViewHolder>() {

    val arrIconViewIds = arrayOf( R.id.imageItem0, R.id.imageItem1, R.id.imageItem2, R.id.imageItem3, R.id.imageItem4, R.id.imageItem5 )

    var cardBGColour: ColorStateList? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): MatchHistoryViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_match_history, parent, false ) as CardView
        return MatchHistoryViewHolder(cardView)
    }

    override fun getItemCount(): Int = dataProvider.getCount()

    override fun onBindViewHolder(holder: MatchHistoryViewHolder, position: Int ) {

        holder.allDisposables.dispose()

        holder.allDisposables = CompositeDisposable()

        with( holder.cardView ) {

            if( cardBGColour == null ) {
                cardBGColour = cardBackgroundColor
            } else {
                setCardBackgroundColor( cardBGColour )
            }

            findViewById<ImageView>(R.id.imgChampionIcon).setImageResource(
                R.drawable.champion_icon_placegolder
            )

            findViewById<ImageView>(R.id.iconPrimaryRune).visibility =
                View.INVISIBLE
            findViewById<ImageView>(R.id.iconSecondaryRunePath).visibility =
                View.INVISIBLE

            findViewById<TextView>(R.id.txtGameMode).text = ""
            findViewById<TextView>(R.id.txtMainKDA).text = "?/?/?"
            findViewById<TextView>(R.id.txtCS).text = "CS: ?"

            findViewById<ImageView>(R.id.iconSummonerSpellD).visibility =
                View.INVISIBLE
            findViewById<ImageView>(R.id.iconSummonerSpellF).visibility =
                View.INVISIBLE

            arrIconViewIds.forEach { findViewById<ImageView>( it ).visibility =
                View.INVISIBLE
            }

            findViewById<ImageView>(R.id.imageWard).visibility =
                View.INVISIBLE
        }

        holder.allDisposables.add( dataProvider.getMatchData( position )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                with( holder.cardView ) {
                    this.setCardBackgroundColor( resources.getColor( if( data.remake ) R.color.bgRemake else if( data.hasWon ) R.color.bgWin else R.color.bgDefeat) )

                    findViewById<ImageView>(R.id.imgChampionIcon).setImageBitmap( data.mainChampionBitmap )

                    if( data.primaryRuneIconResId != null ) {
                        with( findViewById<ImageView>(R.id.iconPrimaryRune) ) { setImageResource(data.primaryRuneIconResId); visibility =
                            View.VISIBLE
                        }
                    }

                    if( data.secondaryRunePathIconResId != null ) {
                        with( findViewById<ImageView>(R.id.iconSecondaryRunePath) ) {
                            setImageResource(data.secondaryRunePathIconResId); visibility = View.VISIBLE
                        }
                    }

                    findViewById<TextView>(R.id.txtGameMode).text = resources.getString( data.gameModeNameResId )
                    findViewById<TextView>(R.id.txtMainKDA).text = "${data.mainKills}/${data.mainDeaths}/${data.mainAssists}"
                    findViewById<TextView>(R.id.txtCS).text = "CS: ${data.creepScore.toString()}"

                    with( findViewById<ImageView>(R.id.iconSummonerSpellD) ){ setImageBitmap( data.bmSummonerSpellD ); visibility =
                        View.VISIBLE
                    }
                    with( findViewById<ImageView>(R.id.iconSummonerSpellF) ){ setImageBitmap( data.bmSummonerSpellF ); visibility =
                        View.VISIBLE
                    }

                    val vlItems = List( arrIconViewIds.size ){ i -> findViewById<ImageView>( arrIconViewIds[ i ] ) }
                    val iconWard = findViewById<ImageView>(R.id.imageWard)

                    for( i in vlItems.indices ) {
                        val imageView = vlItems[ i ]
                        if( i < data.itemIcons.size - 1 ) {
                            imageView.setImageBitmap( data.itemIcons[ i ] )/*.also { visibility = View.VISIBLE }*/
                        } else {
                            imageView.setImageResource(R.drawable.item_empty) // imageView.visibility = View.INVISIBLE
                        }
                        imageView.visibility = View.VISIBLE
                    }

                    if( data.itemIcons.isNotEmpty() ) {
                        iconWard.setImageBitmap( data.itemIcons.last() ).also { visibility = View.VISIBLE }
                    } else {
                        iconWard.setImageResource(R.drawable.item_empty) // iconWard.visibility = View.INVISIBLE
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