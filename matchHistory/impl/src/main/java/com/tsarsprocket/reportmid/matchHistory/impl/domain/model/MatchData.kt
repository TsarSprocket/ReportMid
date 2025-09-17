package com.tsarsprocket.reportmid.matchHistory.impl.domain.model

import com.tsarsprocket.reportmid.lol.api.model.GameType

internal data class MatchData(
    val matchId: String,
    val gameType: GameType,
    val isRemake: Boolean,
    val isWin: Boolean,
    val allPlayers: List<PlayerData>,
    val me: PlayerData,
    val myTeam: TeamData,
    val enemyTeam: TeamData,
    val isNotTheLast: Boolean,
)