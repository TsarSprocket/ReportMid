package com.tsarsprocket.reportmid.findSummonerImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.findSummonerImpl.viewState.ConfirmSummonerViewState
import com.tsarsprocket.reportmid.findSummonerImpl.viewState.SummonerDataEntryViewState
import com.tsarsprocket.reportmid.findSummonerImpl.visualizer.FindSummonerVisualizer
import com.tsarsprocket.reportmid.viewStateApi.di.ViewStateKey
import com.tsarsprocket.reportmid.viewStateApi.visualizer.Visualizer
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal interface VisualizerModule {

    @Binds
    @PerApi
    @IntoMap
    @ViewStateKey(ConfirmSummonerViewState::class)
    fun bindToConfirmSummonerViewState(visualizer: FindSummonerVisualizer): Visualizer

    @Binds
    @PerApi
    @IntoMap
    @ViewStateKey(SummonerDataEntryViewState::class)
    fun bindToSummonerDataEntryViewState(visualizer: FindSummonerVisualizer): Visualizer
}