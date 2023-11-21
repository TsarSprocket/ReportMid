package com.tsarsprocket.reportmid.landing_impl.di

import com.tsarsprocket.reportmid.base.di.PerApi
import com.tsarsprocket.reportmid.landing_impl.reducer.LandingStateReducer
import com.tsarsprocket.reportmid.landing_impl.usecase.LandingUseCase
import com.tsarsprocket.reportmid.landing_impl.usecase.LandingUseCaseImpl
import com.tsarsprocket.reportmid.landing_impl.view.LandingVisualizer
import com.tsarsprocket.reportmid.landing_impl.viewstate.LandingViewIntent
import com.tsarsprocket.reportmid.landing_impl.viewstate.LandingViewState
import com.tsarsprocket.reportmid.view_state_api.di.ViewIntentKey
import com.tsarsprocket.reportmid.view_state_api.di.ViewStateKey
import com.tsarsprocket.reportmid.view_state_api.view_state.StateReducer
import com.tsarsprocket.reportmid.view_state_api.view_state.StateVisualizer
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewIntent
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
internal interface LandingModule {

    @Binds
    @PerApi
    @IntoMap
    @ViewIntentKey(LandingViewIntent::class)
    fun bindLandingStateReducer(reducer: LandingStateReducer): StateReducer<*>

    @Binds
    fun bindLandingUseCase(useCase: LandingUseCaseImpl): LandingUseCase

    @Binds
    @PerApi
    @IntoMap
    @ViewStateKey(LandingViewState::class)
    fun bindLandingVisualizer(visualizer: LandingVisualizer): StateVisualizer<*>

    companion object {

        @Provides
        fun provideStartIntent(): ViewIntent = LandingViewIntent.LandingStartLoadIntent
    }
}
