package com.tsarsprocket.reportmid.matchData.api.data.model

import com.tsarsprocket.reportmid.lol.api.model.GameType

data class Match(
    val matchId: String,
    val gameType: GameType,
    val isRemake: Boolean,
    val teams: List<Team>,
    val participant: List<Participant>,
    val hasMoreHint: HasMoreHint,
)
