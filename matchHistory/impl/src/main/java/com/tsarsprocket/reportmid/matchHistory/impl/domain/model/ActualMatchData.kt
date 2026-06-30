package com.tsarsprocket.reportmid.matchHistory.impl.domain.model

import com.tsarsprocket.reportmid.lol.api.domain.model.GameType

internal data class ActualMatchData(
    val matchId: String,
    val gameType: GameType,
    val isRemake: Boolean,
    val isWin: Boolean,
    val me: PlayerData,
    val myTeam: TeamData,
    val teams: List<TeamData>,
    val isNotTheLast: Boolean,
) : MatchData
