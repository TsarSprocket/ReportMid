package com.tsarsprocket.reportmid.navigationMapImpl.di

import com.tsarsprocket.reportmid.landingApi.navigation.LandingRouteOut
import com.tsarsprocket.reportmid.viewStateApi.navigation.NavigationRoute
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import dagger.Module
import dagger.Provides

@Module
internal class NavigationMapModule {

    @Provides
    @NavigationRoute
    fun provideLandingRouteOut(): (LandingRouteOut) -> ViewIntent = { TODO("Not implemented yet") }
}