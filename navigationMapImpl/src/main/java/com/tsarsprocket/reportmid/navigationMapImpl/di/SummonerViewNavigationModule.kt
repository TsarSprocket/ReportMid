package com.tsarsprocket.reportmid.navigationMapImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.profileOverviewApi.viewIntent.ProfileOverviewViewIntent
import com.tsarsprocket.reportmid.summonerViewApi.navigation.SummonerViewNavigation
import com.tsarsprocket.reportmid.viewStateApi.navigation.Navigation
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import dagger.Module
import dagger.Provides

@Module
class SummonerViewNavigationModule {

    @Provides
    @PerApi
    @Navigation(SummonerViewNavigation.TAG)
    fun SummonerViewNavigation(): SummonerViewNavigation = object : SummonerViewNavigation {

        override fun ViewStateHolder.startProfileOverview(summonerPuuid: String, summonerRegion: Region) {
            postIntent(ProfileOverviewViewIntent(summonerPuuid, summonerRegion))
        }
    }
}