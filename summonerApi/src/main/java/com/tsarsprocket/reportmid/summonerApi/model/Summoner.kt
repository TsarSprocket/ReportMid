package com.tsarsprocket.reportmid.summonerApi.model

import com.tsarsprocket.reportmid.lol.api.model.Puuid
import com.tsarsprocket.reportmid.lol.api.model.Region

class Summoner(
    val region: Region,
    val riotId: String,
    val iconId: Int,
    val puuid: Puuid,
    val level: Int,
) {
    var id: Long? = null
}
