package com.tsarsprocket.reportmid.matchDetails.impl.viewIntent

import com.tsarsprocket.reportmid.lol.api.model.Region
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoadMatchDataIntent(
    val matchId: String,
    val region: Region,
) : AbstractInternalMatchDetailsIntent
