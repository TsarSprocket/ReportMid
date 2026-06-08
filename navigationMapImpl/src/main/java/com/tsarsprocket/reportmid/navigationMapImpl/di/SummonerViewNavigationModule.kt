package com.tsarsprocket.reportmid.navigationMapImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.profileOverviewApi.viewIntent.ProfileOverviewViewIntent
import com.tsarsprocket.reportmid.summonerViewApi.navigation.OngoingSummonerViewNavigation
import com.tsarsprocket.reportmid.summonerViewApi.navigation.StartSummonerViewNavigation
import com.tsarsprocket.reportmid.summonerViewApi.viewIntent.SummonerViewIntent
import com.tsarsprocket.reportmid.viewStateApi.navigation.Navigation
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import dagger.Module
import dagger.Provides

@Module
class SummonerViewNavigationModule {

    @Provides
    @PerApi
    @Navigation(StartSummonerViewNavigation.TAG)
    fun provideStartSummonerViewNavigation(): StartSummonerViewNavigation = object : StartSummonerViewNavigation {

        override fun ViewStateHolder.startProfileOverview(summonerPuuid: String, summonerRegion: Region) {
            postIntent(ProfileOverviewViewIntent(summonerPuuid, summonerRegion))
        }
    }

    @Provides
    @PerApi
    @Navigation(OngoingSummonerViewNavigation.TAG)
    fun provideOngoingSummonerViewNavigation(): OngoingSummonerViewNavigation = object : OngoingSummonerViewNavigation {

        override fun ViewStateHolder.showProfileForSummoner(
            summonerPuuid: String,
            summonerRegion: Region
        ) {
            postIntent(
                intent = SummonerViewIntent(puuid = summonerPuuid, region = summonerRegion),
                returnIntent = currentState.getRestoreStateIntent(),
            )
        }
    }
}