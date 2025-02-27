package com.tsarsprocket.reportmid.viewStateImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.viewStateApi.di.ViewStateKey
import com.tsarsprocket.reportmid.viewStateApi.viewState.EmptyScreenViewState
import com.tsarsprocket.reportmid.viewStateApi.visualizer.StateVisualizer
import com.tsarsprocket.reportmid.viewStateImpl.visualizer.DefaultVisualizer
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface VisualizerModule {

    @Binds
    @PerApi
    @IntoMap
    @ViewStateKey(EmptyScreenViewState::class)
    fun bindToEmptyScreen(visualizer: DefaultVisualizer): StateVisualizer
}