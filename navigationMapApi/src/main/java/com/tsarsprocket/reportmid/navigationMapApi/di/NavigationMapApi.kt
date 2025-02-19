package com.tsarsprocket.reportmid.navigationMapApi.di

import com.tsarsprocket.reportmid.findSummonerApi.navigation.FindSummonerNavigation
import com.tsarsprocket.reportmid.landingApi.navigation.LandingNavigation
import com.tsarsprocket.reportmid.viewStateApi.navigation.Navigation
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent

interface NavigationMapApi {

    @Navigation(FindSummonerNavigation.TAG)
    fun getFindSummonerNavigation(): FindSummonerNavigation

    @Navigation(LandingNavigation.TAG)
    fun getLandingNavigation(): LandingNavigation

    fun getStartViewIntentCreator(): ViewIntent
}