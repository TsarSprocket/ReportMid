package com.tsarsprocket.reportmid.landingApi.navigation

import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder

interface LandingNavigation {

    fun ViewStateHolder.findSummoner(): Unit

    fun ViewStateHolder.proceed(): Unit

    companion object {
        const val TAG = "landing_navigation_tag"
    }
}