package com.tsarsprocket.reportmid.matchHistory.impl.domain.model

internal data class TeamData(
    val players: List<PlayerData>,
    val isWinner: Boolean,
)
