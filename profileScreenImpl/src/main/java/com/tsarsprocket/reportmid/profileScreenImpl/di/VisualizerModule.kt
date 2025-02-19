package com.tsarsprocket.reportmid.profileScreenImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.profileScreenImpl.viewState.ProfileScreenViewState
import com.tsarsprocket.reportmid.profileScreenImpl.visualizer.ProfileScreenVisualizer
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
    @ViewStateKey(ProfileScreenViewState::class)
    fun bindToProfileScreenViewState(visualizer: ProfileScreenVisualizer): Visualizer
}