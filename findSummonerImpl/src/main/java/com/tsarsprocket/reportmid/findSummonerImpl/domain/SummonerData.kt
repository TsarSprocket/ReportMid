package com.tsarsprocket.reportmid.findSummonerImpl.domain

import com.tsarsprocket.reportmid.lol.model.Puuid
import com.tsarsprocket.reportmid.lol.model.Region

data class SummonerData(
    val puuid: Puuid,
    val region: Region,
    val riotId: String,
    val iconUrl: String,
    val level: Long,
)