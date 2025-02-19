package com.tsarsprocket.reportmid.navigationMapImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.findSummonerApi.viewIntent.FindSummonerViewIntent
import com.tsarsprocket.reportmid.landingApi.navigation.LandingNavigation
import com.tsarsprocket.reportmid.landingApi.viewIntent.LandingIntent
import com.tsarsprocket.reportmid.profileScreenApi.viewIntent.ShowProfileScreenViewIntent
import com.tsarsprocket.reportmid.viewStateApi.navigation.Navigation
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder
import dagger.Module
import dagger.Provides

@Module
internal class LandingNavigationModule {

    @Provides
    @PerApi
    @Navigation(LandingNavigation.TAG)
    fun provideLandingNavigation(): LandingNavigation = object : LandingNavigation {

        override fun ViewStateHolder.findSummoner() = postIntent(FindSummonerViewIntent, returnIntent = LandingIntent.QuitViewIntent)

        override fun ViewStateHolder.proceed() = postIntent(ShowProfileScreenViewIntent)
    }
}