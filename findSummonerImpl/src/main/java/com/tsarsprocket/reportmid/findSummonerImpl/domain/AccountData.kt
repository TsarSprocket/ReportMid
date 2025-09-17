package com.tsarsprocket.reportmid.findSummonerImpl.domain

import com.tsarsprocket.reportmid.lol.api.model.Puuid
import com.tsarsprocket.reportmid.lol.api.model.Region

internal data class AccountData(
    val gameName: String,
    val tagline: String,
    val puuid: Puuid,
    val region: Region,
    val isAlreadyInUse: Boolean,
)
