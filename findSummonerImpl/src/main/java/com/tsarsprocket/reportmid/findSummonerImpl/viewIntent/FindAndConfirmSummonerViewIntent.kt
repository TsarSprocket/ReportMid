package com.tsarsprocket.reportmid.findSummonerImpl.viewIntent

import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class FindAndConfirmSummonerViewIntent(
    val gameName: String,
    val tagline: String,
    val region: Region,
) : ViewIntent