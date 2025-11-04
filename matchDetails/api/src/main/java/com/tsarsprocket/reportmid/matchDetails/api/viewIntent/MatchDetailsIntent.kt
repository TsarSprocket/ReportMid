package com.tsarsprocket.reportmid.matchDetails.api.viewIntent

import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import kotlinx.parcelize.Parcelize

@Parcelize
data class MatchDetailsIntent(
    val matchId: String,
    val region: Region,
) : ViewIntent
