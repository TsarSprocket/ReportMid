package com.tsarsprocket.reportmid.matchDetails.impl.domain.model

import com.tsarsprocket.reportmid.lol.api.domain.model.GameType
import com.tsarsprocket.reportmid.lol.api.domain.model.Region

internal data class MatchDetailsData(
    val region: Region,
    val gameType: GameType,
    val duration: Long,
    val teams: List<TeamData>,
)
