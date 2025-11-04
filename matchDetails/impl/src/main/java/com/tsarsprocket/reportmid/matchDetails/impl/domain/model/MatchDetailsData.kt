package com.tsarsprocket.reportmid.matchDetails.impl.domain.model

import com.tsarsprocket.reportmid.lol.api.model.GameType
import com.tsarsprocket.reportmid.lol.api.model.Region

internal data class MatchDetailsData(
    val region: Region,
    val gameType: GameType,
)
