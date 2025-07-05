package com.tsarsprocket.reportmid.landingApi.viewIntent

import com.tsarsprocket.reportmid.lol.api.model.Puuid
import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

sealed interface LandingIntent : ViewIntent {

    @Parcelize
    data object LandingStartLoadViewIntent : LandingIntent

    @Parcelize
    data class SummonerFoundViewIntent(
        val puuid: @RawValue Puuid,
        val region: Region,
    ) : LandingIntent

    @Parcelize
    data object QuitViewIntent : LandingIntent
}