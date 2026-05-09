package com.tsarsprocket.reportmid.matchUpView.impl.domain.model

import com.tsarsprocket.reportmid.lol.api.domain.model.GameType
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
internal data class CurrentMatchUp(
    val gameType: GameType,
    val gameStartTime: Instant,
    val teams: List<Team>,
) : MatchUpDomainModel
