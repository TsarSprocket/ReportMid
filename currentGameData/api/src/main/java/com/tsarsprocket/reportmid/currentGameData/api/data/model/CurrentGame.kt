package com.tsarsprocket.reportmid.currentGameData.api.data.model

import com.tsarsprocket.reportmid.lol.api.domain.model.GameType
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

sealed interface CurrentGame {

    @OptIn(ExperimentalTime::class)
    data class InProgress(
        val gameId: Long,
        val gameType: GameType,
        val gameStartTime: Instant,
        val teams: List<CurrentTeam>,
    ) : CurrentGame

    data object NotFound : CurrentGame
}
