package com.tsarsprocket.reportmid.matchHistory.impl.domain.model

import com.tsarsprocket.reportmid.lol.api.model.GameType

internal data class MatchData(
    val matchId: String,
    val gameType: GameType,
    val isRemake: Boolean,
    val isWin: Boolean,
    val me: PlayerData,
    val myTeam: TeamData,
    val teams: List<TeamData>,
    val isNotTheLast: Boolean,
)