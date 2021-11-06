package com.tsarsprocket.reportmid.summoner.model

import android.graphics.drawable.Drawable
import com.merakianalytics.orianna.Orianna
import com.merakianalytics.orianna.types.common.Queue
import com.merakianalytics.orianna.types.core.championmastery.ChampionMastery
import com.tsarsprocket.reportmid.model.*
import com.tsarsprocket.reportmid.model.my_account.MyAccountModel
import com.tsarsprocket.reportmid.riotapi.summoner.SummonerDto
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class SummonerModel @AssistedInject constructor(
    @Assisted val region: RegionModel,
    @Assisted summonerDto: SummonerDto,
    @Assisted val masteries: Single<List<ChampionMasteryModel>>,
    private val repository: Repository,
) {
    val id: String = summonerDto.id
    val name: String = summonerDto.name
    val icon: Single<Drawable> by lazy { Single.fromCallable { summonerDto.profileIconId }.subscribeOn( Schedulers.io() ).flatMap { repository.iconProvider.getProfileIcon(it) }  }
    val puuid: String = summonerDto.puuid
    val level: Long = summonerDto.summonerLevel
    val soloQueuePosition by lazy{ repository.getLeaguePosition { Orianna.leaguePositionsForSummoner( Orianna.summonerWithId( id ).get()).get().find { it?.queue == Queue.RANKED_SOLO }  } }
    val myAccount: Maybe<MyAccountModel> by lazy { repository.getMyAccountForSummoner( this ) }
    val puuidAndRegion: PuuidAndRegion by lazy { PuuidAndRegion(puuid,region) }
    val riotAccountId: String = summonerDto.accountId

    fun getMasteryWithChampion( championModel: ChampionModel): Observable<ChampionMastery> =
        Observable.fromCallable { ChampionMastery.forSummoner( Orianna.summonerWithId( id ).get() ).withChampion( Orianna.championWithId(championModel.id).get() ).get() }.subscribeOn( Schedulers.io() )

    fun getCurrentMatch() = repository.getCurrentMatch(this)

    fun getMatchHistory() = repository.getMatchHistoryModel(region, this)

    //  Classes  //////////////////////////////////////////////////////////////

    class ByNameAndRegionComparator: Comparator<SummonerModel> {
        override fun compare(o1: SummonerModel, o2: SummonerModel): Int {
            val byName = o1.name.compareTo(o2.name)
            if( byName != 0 ) return byName
            return o1.region.compareTo(o2.region)
        }
    }
}
