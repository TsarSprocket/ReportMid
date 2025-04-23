package com.tsarsprocket.reportmid.navigationMapImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.mainScreenApi.navigation.MainScreenNavigation
import com.tsarsprocket.reportmid.summonerViewApi.viewIntent.SummonerViewIntent
import com.tsarsprocket.reportmid.viewStateApi.navigation.Navigation
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import dagger.Module
import dagger.Provides

@Module
class MainScreenNavigationModule {

    @Provides
    @PerApi
    @Navigation(MainScreenNavigation.TAG)
    fun provideMainScreenNavigation(): MainScreenNavigation = object : MainScreenNavigation {

        override fun ViewStateHolder.startSummonerView(puuid: String, region: Region) = postIntent(SummonerViewIntent(puuid, region))
    }
}