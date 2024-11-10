package com.tsarsprocket.reportmid.navigationMapApi.di

import com.tsarsprocket.reportmid.landingApi.navigation.LandingRouteOut
import com.tsarsprocket.reportmid.viewStateApi.navigation.NavigationRoute
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent

interface NavigationMapApi {
    @NavigationRoute
    fun landingRouteOut(): (LandingRouteOut) -> ViewIntent
}