package com.tsarsprocket.reportmid.matchDetails.impl.domain.model

import com.tsarsprocket.reportmid.lol.api.domain.model.TeamSide

internal data class TeamData(
    val players: List<PlayerData>,
    val gameOutcome: GameOutcome,
    val teamSide: TeamSide,
)