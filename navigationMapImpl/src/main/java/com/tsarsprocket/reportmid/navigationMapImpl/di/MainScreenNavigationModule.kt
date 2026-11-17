package com.tsarsprocket.reportmid.navigationMapImpl.di

import com.tsarsprocket.reportmid.appBar.api.navigation.AppBarInitialViewState
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.mainScreenApi.navigation.MainScreenNavigation
import com.tsarsprocket.reportmid.summonerViewApi.viewIntent.SummonerViewIntent
import com.tsarsprocket.reportmid.viewStateApi.navigation.Navigation
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
class MainScreenNavigationModule {

    @Provides
    @PerApi
    @Navigation(MainScreenNavigation.TAG)
    fun provideMainScreenNavigation(
        @AppBarInitialViewState
        appBarInitialViewStateProvider: Provider<ViewState>
    ): MainScreenNavigation = object : MainScreenNavigation {

        override val appBarInitialState: ViewState
            get() = appBarInitialViewStateProvider.get()

        override fun ViewStateHolder.startSummonerView(puuid: String, region: Region) = postIntent(SummonerViewIntent(puuid, region))
    }
}