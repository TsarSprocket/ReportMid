package com.tsarsprocket.reportmid.mainScreenApi.viewIntent

import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import kotlinx.parcelize.Parcelize


@Parcelize
data class MainScreenViewIntent(
    val puuid: String,
    val region: Region,
) : ViewIntent
