package com.tsarsprocket.reportmid.summonerApi.model

import com.tsarsprocket.reportmid.lol.api.domain.model.Puuid
import com.tsarsprocket.reportmid.lol.api.domain.model.PuuidAndRegion
import com.tsarsprocket.reportmid.lol.api.domain.model.Region

interface SummonerInfo {
    val id: Long
    val puuid: Puuid
    val region: Region
}

val SummonerInfo.puuidAndRegion: PuuidAndRegion
    get() = PuuidAndRegion(puuid, region)
