package com.tsarsprocket.reportmid.model

import android.graphics.drawable.Drawable
import android.util.Log
import com.merakianalytics.orianna.Orianna
import com.merakianalytics.orianna.types.common.Queue
import com.merakianalytics.orianna.types.core.championmastery.ChampionMastery
import com.merakianalytics.orianna.types.core.match.MatchHistory
import com.merakianalytics.orianna.types.core.spectator.CurrentMatch
import com.merakianalytics.orianna.types.core.summoner.Summoner
import com.tsarsprocket.reportmid.model.state.MyAccountModel
import com.tsarsprocket.reportmid.riotapi.spectatorV4.SpectatorV4Service
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class SummonerModel(
    val repository: Repository,
    private val shadowSummoner: Summoner
) {
    val id: String = shadowSummoner.id
    val name: String = shadowSummoner.name
    val icon: Single<Drawable> by lazy { Single.fromCallable { shadowSummoner.profileIcon.id }.subscribeOn(Schedulers.io()).flatMap { repository.iconProvider.getProfileIcon(it) }  }
    val puuid: String = shadowSummoner.puuid
    val level: Int = shadowSummoner.level
    val masteries: Observable<List<Observable<ChampionMasteryModel>>> by lazy { getObservableMasteryList().replay( 1 ).autoConnect() }
    val matchHistory: Observable<MatchHistoryModel> by lazy { getObservableMatchHistoryForSummoner( shadowSummoner ).replay( 1 ).autoConnect() }
    val soloQueuePosition by lazy{ repository.getLeaguePosition { shadowSummoner.getLeaguePosition( Queue.RANKED_SOLO ) } }
    val region by lazy{ Repository.getRegion( shadowSummoner.region ) }
    val myAccount: Maybe<MyAccountModel> by lazy { repository.getMyAccountForSummoner(this) }
    val puuidAndRegion: PuuidAndRegion by lazy { PuuidAndRegion(puuid,region) }

    fun getMasteryWithChampion( championModel: ChampionModel ): Observable<ChampionMastery> =
        Observable.fromCallable { ChampionMastery.forSummoner( shadowSummoner ).withChampion( Orianna.championWithId(championModel.id).get() ).get() }.subscribeOn( Schedulers.io() )

    fun getCurrentMatch() = repository.getCurrentMatch(this)

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
