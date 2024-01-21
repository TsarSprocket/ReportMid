package com.tsarsprocket.reportmid.summoner_api.model

import com.merakianalytics.orianna.Orianna
import com.merakianalytics.orianna.types.core.championmastery.ChampionMastery
import com.tsarsprocket.reportmid.lol.model.Champion
import com.tsarsprocket.reportmid.lol.model.Puuid
import com.tsarsprocket.reportmid.lol.model.PuuidAndRegion
import com.tsarsprocket.reportmid.lol.model.Region
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class SummonerModel(
    val region: Region,
    val id: String,
    val name: String,
    val iconId: Int,
    val puuid: Puuid,
    val level: Long,
    val riotAccountId: String,
    val masteries: Single<List<ChampionMasteryModel>>,
) {
    val puuidAndRegion: PuuidAndRegion by lazy { PuuidAndRegion(puuid, region) }

    fun getMasteryWithChampion(champion: Champion): Observable<ChampionMastery> =
        Observable.fromCallable {
            ChampionMastery.forSummoner(Orianna.summonerWithId(id).get())
                .withChampion(Orianna.championWithId(champion.id).get()).get()
        }.subscribeOn(Schedulers.io())

    //  Classes  //////////////////////////////////////////////////////////////

    class ByNameAndRegionComparator : Comparator<SummonerModel> {
        override fun compare(o1: SummonerModel, o2: SummonerModel): Int {
            val byName = o1.name.compareTo(o2.name)
            if(byName != 0) return byName
            return o1.region.compareTo(o2.region)
        }
    }
}
