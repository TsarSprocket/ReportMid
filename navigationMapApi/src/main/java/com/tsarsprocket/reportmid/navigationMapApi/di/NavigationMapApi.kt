package com.tsarsprocket.reportmid.navigationMapApi.di

import com.tsarsprocket.reportmid.landingApi.navigation.LandingRouteOut
import com.tsarsprocket.reportmid.viewStateApi.navigation.NavigationRoute
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent

interface NavigationMapApi {

    fun getStartViewIntentCreator(): () -> ViewIntent

    @NavigationRoute
    fun getLandingRouteOut(): (LandingRouteOut) -> ViewIntent
}