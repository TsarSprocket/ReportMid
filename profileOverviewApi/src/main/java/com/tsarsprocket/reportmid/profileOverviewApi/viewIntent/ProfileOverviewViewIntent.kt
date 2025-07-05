package com.tsarsprocket.reportmid.profileOverviewApi.viewIntent

import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileOverviewViewIntent(
    val summonerPuuid: String,
    val summonerRegion: Region,
    val isRetry: Boolean = false,
) : ViewIntent
