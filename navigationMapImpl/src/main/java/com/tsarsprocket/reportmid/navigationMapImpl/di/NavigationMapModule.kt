package com.tsarsprocket.reportmid.navigationMapImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.landingApi.viewIntent.LandingIntent.LandingStartLoadViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import dagger.Module
import dagger.Provides

@Module
internal class NavigationMapModule {

    @Provides
    @PerApi
    fun bindStartViewIntent(): ViewIntent = LandingStartLoadViewIntent
}