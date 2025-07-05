package com.tsarsprocket.reportmid.summonerApi.model

import com.tsarsprocket.reportmid.lol.api.model.GameName
import com.tsarsprocket.reportmid.lol.api.model.Puuid
import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.lol.api.model.TagLine

data class RiotAccount(
    val puuid: Puuid,
    val region: Region,
    val gameName: GameName,
    val tagLine: TagLine,
)
