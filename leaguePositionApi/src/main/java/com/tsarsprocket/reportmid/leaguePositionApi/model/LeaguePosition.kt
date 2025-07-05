package com.tsarsprocket.reportmid.leaguePositionApi.model

import com.tsarsprocket.reportmid.lol.api.model.Division
import com.tsarsprocket.reportmid.lol.api.model.QueueType
import com.tsarsprocket.reportmid.lol.api.model.Tier

data class LeaguePosition(
    val queueType: QueueType,
    val tier: Tier,
    val division: Division,
    val wins: Int,
    val losses: Int,
) {
    val totalGames: Int
        get() = wins + losses
}
