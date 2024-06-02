package com.tsarsprocket.reportmid.summonerApi.model

import com.tsarsprocket.reportmid.lol.model.Puuid
import com.tsarsprocket.reportmid.lol.model.PuuidAndRegion
import com.tsarsprocket.reportmid.lol.model.Region

interface SummonerInfo {
    val id: Long
    val puuid: Puuid
    val region: Region
}

val SummonerInfo.puuidAndRegion: PuuidAndRegion
    get() = PuuidAndRegion(puuid, region)
