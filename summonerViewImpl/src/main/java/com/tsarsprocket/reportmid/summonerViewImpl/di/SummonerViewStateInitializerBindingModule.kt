package com.tsarsprocket.reportmid.summonerViewImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.summonerViewImpl.stateInitializer.SummonerViewStateInitializer
import com.tsarsprocket.reportmid.summonerViewImpl.viewState.SummonerViewState
import com.tsarsprocket.reportmid.viewStateApi.di.ViewStateKey
import com.tsarsprocket.reportmid.viewStateApi.stateInitializer.ViewStateInitializer
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface SummonerViewStateInitializerBindingModule {

    @Binds
    @PerApi
    @IntoMap
    @ViewStateKey(SummonerViewState::class)
    fun bindSummonerViewStateInitializer(initializer: SummonerViewStateInitializer): ViewStateInitializer
}
