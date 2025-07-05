package com.tsarsprocket.reportmid.summonerViewApi.viewIntent

import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import kotlinx.parcelize.Parcelize

@Parcelize
data class SummonerViewIntent(
    val puuid: String,
    val region: Region,
) : ViewIntent
