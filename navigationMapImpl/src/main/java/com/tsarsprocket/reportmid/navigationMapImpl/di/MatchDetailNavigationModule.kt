package com.tsarsprocket.reportmid.navigationMapImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchDetails.api.navigation.MatchDetailNavigation
import com.tsarsprocket.reportmid.summonerViewApi.viewIntent.SummonerViewIntent
import com.tsarsprocket.reportmid.viewStateApi.navigation.Navigation
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import dagger.Module
import dagger.Provides

@Module
class MatchDetailNavigationModule {

    @Provides
    @PerApi
    @Navigation(MatchDetailNavigation.TAG)
    fun provideMatchDetailNavigation(): MatchDetailNavigation = object : MatchDetailNavigation {

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