package com.tsarsprocket.reportmid.league_position_api.model

import com.tsarsprocket.reportmid.lol.model.Division
import com.tsarsprocket.reportmid.lol.model.QueueType
import com.tsarsprocket.reportmid.lol.model.Tier

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
