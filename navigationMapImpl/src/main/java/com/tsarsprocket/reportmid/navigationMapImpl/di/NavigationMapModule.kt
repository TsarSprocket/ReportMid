package com.tsarsprocket.reportmid.navigationMapImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.landingApi.navigation.LandingRouteOut
import com.tsarsprocket.reportmid.landingApi.viewIntent.LandingViewIntent
import com.tsarsprocket.reportmid.profileScreenApi.viewIntent.ShowProfileScreenViewIntent
import com.tsarsprocket.reportmid.viewStateApi.navigation.NavigationRoute
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
internal interface NavigationMapModule {

    @Binds
    @PerApi
    fun bindStartViewIntentCreator(creator: () -> LandingViewIntent): () -> ViewIntent

    companion object {

        @Provides
        @PerApi
        @NavigationRoute
        fun provideLandingRouteOut(viewIntentCreator: () -> ShowProfileScreenViewIntent): (LandingRouteOut) -> ViewIntent = { viewIntentCreator() }
    }
}