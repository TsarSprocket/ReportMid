package com.tsarsprocket.reportmid.model

import android.graphics.Bitmap
import com.merakianalytics.orianna.types.core.match.MatchHistory
import com.merakianalytics.orianna.types.core.summoner.Summoner
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

data class SummonerModel(
    val repository: Repository,
    val shadowSummoner: Summoner
) {

    val name: String = shadowSummoner.name
    val icon: Observable<Bitmap> by lazy { Observable.fromCallable { shadowSummoner.profileIcon.image.get()!! }.subscribeOn( Schedulers.io() ).replay( 1 ).autoConnect() }
    val puuid: String = shadowSummoner.puuid
    val level: Int = shadowSummoner.level
    val masteries: Observable<List<Observable<ChampionMasteryModel>>> by lazy { getObservableMasteryList().replay( 1 ).autoConnect() }
    val matchHistory: Observable<MatchHistoryModel> by lazy { getObservableMatchHistoryForSummoner( shadowSummoner ).replay( 1 ).autoConnect() }

    private fun getObservableMasteryList() = Observable.fromCallable {
        List( shadowSummoner.championMasteries.size ) { i ->
            repository.getChampionMasteryModel( shadowSummoner.championMasteries[ i ] ).subscribeOn( Schedulers.io() ).replay( 1 ).autoConnect()
        }
    }.subscribeOn( Schedulers.io() )

    private fun getObservableMatchHistoryForSummoner( summoner: Summoner ): Observable<MatchHistoryModel> =
        Observable.fromCallable { MatchHistory.forSummoner( summoner ).get()!! }
            .subscribeOn( Schedulers.io() )
            .flatMap { shadowHistory -> repository.getMatchHistoryModel( shadowHistory ) }
}
