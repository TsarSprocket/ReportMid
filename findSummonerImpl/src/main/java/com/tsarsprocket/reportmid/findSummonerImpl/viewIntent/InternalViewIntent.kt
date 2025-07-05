package com.tsarsprocket.reportmid.findSummonerImpl.viewIntent

import com.tsarsprocket.reportmid.kspApi.annotation.Intent
import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import kotlinx.parcelize.Parcelize

internal sealed interface InternalViewIntent : ViewIntent {

    @Parcelize
    @Intent
    data class FindAndConfirmSummonerViewIntent(
        val gameName: String,
        val tagline: String,
        val region: Region,
    ) : InternalViewIntent

    @Parcelize
    @Intent
    data class GameNameChanged(
        val newGameName: String
    ) : InternalViewIntent

    @Parcelize
    @Intent
    data class RegionSelected(
        val newRegionId: Int
    ) : InternalViewIntent

    @Parcelize
    @Intent
    data class TagLineChanged(
        val newTagline: String
    ) : InternalViewIntent
}