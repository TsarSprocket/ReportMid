package com.tsarsprocket.reportmid.summonerApi.model

import com.tsarsprocket.reportmid.lol.api.domain.model.Puuid
import com.tsarsprocket.reportmid.lol.api.domain.model.Region

class Summoner(
    val region: Region,
    val iconId: Int,
    val puuid: Puuid,
    val level: Int,
) {
    var id: Long? = null
}
