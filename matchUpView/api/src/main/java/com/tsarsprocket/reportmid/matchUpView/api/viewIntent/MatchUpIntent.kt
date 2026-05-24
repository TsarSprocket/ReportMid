package com.tsarsprocket.reportmid.matchUpView.api.viewIntent

import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import kotlinx.parcelize.Parcelize

@Parcelize
data class MatchUpIntent(
    val puuid: String,
    val region: Region,
) : ViewIntent
