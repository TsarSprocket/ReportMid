package com.tsarsprocket.reportmid.findSummonerImpl.domain

import com.tsarsprocket.reportmid.lol.api.model.GameName
import com.tsarsprocket.reportmid.lol.api.model.Puuid
import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.lol.api.model.TagLine

internal data class AccountData(
    val gameName: GameName,
    val tagline: TagLine,
    val puuid: Puuid,
    val region: Region,
    val isAlreadyInUse: Boolean,
)
