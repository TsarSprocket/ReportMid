package com.tsarsprocket.reportmid.landingImpl.viewIntent

import com.tsarsprocket.reportmid.kspApi.annotation.Intent
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import kotlinx.parcelize.Parcelize

internal sealed interface InternalLandingIntent : ViewIntent {

    @Intent
    @Parcelize
    data object DataDragonNotLoadedViewIntent : InternalLandingIntent
}