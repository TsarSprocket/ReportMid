package com.tsarsprocket.reportmid.matchData.api.data.model

import com.tsarsprocket.reportmid.lol.api.domain.model.GameType

data class Match(
    val matchId: String,
    val gameType: GameType,
    val duration: Long,
    val isRemake: Boolean,
    val teams: List<Team>,
    val participant: List<Participant>,
)
