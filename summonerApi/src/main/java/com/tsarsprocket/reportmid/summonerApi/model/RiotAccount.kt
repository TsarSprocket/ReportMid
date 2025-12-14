package com.tsarsprocket.reportmid.summonerApi.model

import com.tsarsprocket.reportmid.lol.api.domain.model.Puuid
import com.tsarsprocket.reportmid.lol.api.domain.model.Region

data class RiotAccount(
    val puuid: Puuid,
    val region: Region,
    val gameName: String,
    val tagLine: String,
)
