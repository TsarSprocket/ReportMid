package com.tsarsprocket.reportmid.model

import android.graphics.Bitmap
import com.merakianalytics.orianna.types.common.Queue
import com.merakianalytics.orianna.types.core.championmastery.ChampionMastery
import com.merakianalytics.orianna.types.core.match.MatchHistory
import com.merakianalytics.orianna.types.core.spectator.CurrentMatch
import com.merakianalytics.orianna.types.core.summoner.Summoner
import com.tsarsprocket.reportmid.model.state.MyAccountModel
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class SummonerModel(
    val repository: Repository,
    private val shadowSummoner: Summoner
) {

    val name: String = shadowSummoner.name
    val icon: Observable<Bitmap> by lazy { Observable.fromCallable { shadowSummoner.profileIcon.image.get()!! }.subscribeOn( Schedulers.io() ).replay( 1 ).autoConnect() }
    val puuid: String = shadowSummoner.puuid
    val level: Int = shadowSummoner.level
    val masteries: Observable<List<Observable<ChampionMasteryModel>>> by lazy { getObservableMasteryList().replay( 1 ).autoConnect() }
    val matchHistory: Observable<MatchHistoryModel> by lazy { getObservableMatchHistoryForSummoner( shadowSummoner ).replay( 1 ).autoConnect() }
    val soloQueuePosition by lazy{ repository.getLeaguePosition { shadowSummoner.getLeaguePosition( Queue.RANKED_SOLO ) } }
    val region by lazy{ Repository.getRegion( shadowSummoner.region ) }
    val myAccount: Maybe<MyAccountModel> by lazy { repository.getMyAccountForSummoner(this) }

    fun getMasteryWithChampion( championModel: ChampionModel ): Observable<ChampionMastery> =
        Observable.fromCallable { ChampionMastery.forSummoner( shadowSummoner ).withChampion( championModel.shadowChampion ).get() }.subscribeOn( Schedulers.io() )

    fun getCurrentMatch() = repository.getCurrentMatch{
        val currentMatch = CurrentMatch.forSummoner( shadowSummoner ).get()
        if( currentMatch.exists() ) currentMatch else throw MatchIsNotInProgressException()
    }

    private fun getObservableMasteryList() = Observable.fromCallable {
        List( shadowSummoner.championMasteries.size ) { i ->
            repository.getChampionMasteryModel( shadowSummoner.championMasteries[ i ] ).subscribeOn( Schedulers.io() ).replay( 1 ).autoConnect()
        }
    }.subscribeOn( Schedulers.io() )

    private fun getObservableMatchHistoryForSummoner( summoner: Summoner ): Observable<MatchHistoryModel> =
        Observable.fromCallable { MatchHistory.forSummoner( summoner ).get()!! }
            .subscribeOn( Schedulers.io() )
            .switchMap { shadowHistory -> repository.getMatchHistoryModel( shadowHistory ) }

    //  Classes  //////////////////////////////////////////////////////////////

    class ByNameAndRegionComparator: Comparator<SummonerModel> {
        override fun compare(o1: SummonerModel, o2: SummonerModel): Int {
            val byName = o1.name.compareTo(o2.name)
            if( byName != 0 ) return byName
            return o1.region.compareTo(o2.region)
        }
    }
}
