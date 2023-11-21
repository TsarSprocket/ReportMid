package com.tsarsprocket.reportmid.landing_impl.viewstate

import com.tsarsprocket.reportmid.view_state_api.view_state.ViewIntent

sealed class LandingViewIntent : ViewIntent {
    override val clusterClass = LandingViewIntent::class

    object LandingStartLoadIntent : LandingViewIntent()
}