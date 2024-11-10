package com.tsarsprocket.reportmid.findSummonerImpl.domain

import com.tsarsprocket.reportmid.lol.model.Puuid
import com.tsarsprocket.reportmid.lol.model.Region

internal data class AccountData(
    val puuid: Puuid,
    val region: Region,
    val isAlreadyInUse: Boolean,
)
