package com.tsarsprocket.reportmid.summonerApi.model

import com.tsarsprocket.reportmid.lol.model.GameName
import com.tsarsprocket.reportmid.lol.model.Puuid
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.lol.model.TagLine

data class RiotAccount(
    val puuid: Puuid,
    val region: Region,
    val gameName: GameName,
    val tagLine: TagLine,
)
