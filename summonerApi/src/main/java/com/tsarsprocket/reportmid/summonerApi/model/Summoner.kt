package com.tsarsprocket.reportmid.summonerApi.model

import com.merakianalytics.orianna.Orianna
import com.merakianalytics.orianna.types.core.championmastery.ChampionMastery
import com.tsarsprocket.reportmid.lol.model.Champion
import com.tsarsprocket.reportmid.lol.model.Puuid
import com.tsarsprocket.reportmid.lol.model.PuuidAndRegion
import com.tsarsprocket.reportmid.lol.model.Region
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class Summoner(
    val region: Region,
    val riotId: String,
    val iconId: Int,
    val puuid: Puuid,
    val level: Int,
    val masteriesProvider: MasteriesProvider,
) {
    var id: Long? = null
    val puuidAndRegion: PuuidAndRegion by lazy { PuuidAndRegion(puuid, region) }

    fun getMasteryWithChampion(champion: Champion): Observable<ChampionMastery> =
        Observable.fromCallable {
            ChampionMastery.forSummoner(Orianna.summonerWithId(riotId).get())
                .withChampion(Orianna.championWithId(champion.id.toInt()).get()).get()
        }.subscribeOn(Schedulers.io())
}
