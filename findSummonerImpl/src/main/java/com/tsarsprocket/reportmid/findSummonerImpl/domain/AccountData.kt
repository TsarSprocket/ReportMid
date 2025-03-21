package com.tsarsprocket.reportmid.findSummonerImpl.domain

import com.tsarsprocket.reportmid.lol.model.GameName
import com.tsarsprocket.reportmid.lol.model.Puuid
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.lol.model.TagLine

internal data class AccountData(
    val gameName: GameName,
    val tagline: TagLine,
    val puuid: Puuid,
    val region: Region,
    val isAlreadyInUse: Boolean,
)
