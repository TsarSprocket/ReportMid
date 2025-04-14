package com.tsarsprocket.reportmid.navigationMapImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.findSummonerApi.viewIntent.FindSummonerViewIntent
import com.tsarsprocket.reportmid.landingApi.navigation.LandingNavigation
import com.tsarsprocket.reportmid.landingApi.viewIntent.LandingIntent
import com.tsarsprocket.reportmid.lol.model.Puuid
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.mainScreenApi.viewIntent.MainScreenViewIntent
import com.tsarsprocket.reportmid.viewStateApi.navigation.Navigation
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import dagger.Module
import dagger.Provides

@Module
internal class LandingNavigationModule {

    @Provides
    @PerApi
    @Navigation(LandingNavigation.TAG)
    fun provideLandingNavigation(): LandingNavigation = object : LandingNavigation {

        override fun ViewStateHolder.findSummoner() = postIntent(FindSummonerViewIntent, returnIntent = LandingIntent.QuitViewIntent)

        override fun ViewStateHolder.proceed(puuid: Puuid, region: Region) = postIntent(MainScreenViewIntent(puuid.value, region))
    }
}