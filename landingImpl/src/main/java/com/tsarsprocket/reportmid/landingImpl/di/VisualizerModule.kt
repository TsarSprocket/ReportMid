package com.tsarsprocket.reportmid.landingImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.landingImpl.viewState.InternalLandingViewState.DataDragonNotLoadedViewState
import com.tsarsprocket.reportmid.landingImpl.viewState.InternalLandingViewState.LandingPageViewState
import com.tsarsprocket.reportmid.landingImpl.visualizer.LandingVisualizer
import com.tsarsprocket.reportmid.viewStateApi.di.ViewStateKey
import com.tsarsprocket.reportmid.viewStateApi.visualizer.StateVisualizer
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface VisualizerModule {

    @Binds
    @PerApi
    @IntoMap
    @ViewStateKey(DataDragonNotLoadedViewState::class)
    fun bindToDataDragonNotLoadedViewState(visualizer: LandingVisualizer): StateVisualizer

    @Binds
    @PerApi
    @IntoMap
    @ViewStateKey(LandingPageViewState::class)
    fun bindToLandingViewState(visualizer: LandingVisualizer): StateVisualizer
}