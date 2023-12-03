package com.tsarsprocket.reportmid.landing_impl.di

import com.tsarsprocket.reportmid.landing_impl.usecase.LandingUseCase
import com.tsarsprocket.reportmid.landing_impl.usecase.LandingUseCaseImpl
import com.tsarsprocket.reportmid.landing_impl.viewstate.LandingViewIntent
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewIntent
import dagger.Binds
import dagger.Module

@Module
internal interface LandingModule {

    @Binds
    fun bindLandingUseCase(useCase: LandingUseCaseImpl): LandingUseCase

    @Binds
    fun bindStartIntent(intent: LandingViewIntent.LandingStartLoadIntent): ViewIntent
}
