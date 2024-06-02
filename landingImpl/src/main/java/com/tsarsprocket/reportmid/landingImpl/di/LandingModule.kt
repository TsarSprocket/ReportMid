package com.tsarsprocket.reportmid.landingImpl.di

import com.tsarsprocket.reportmid.landingImpl.usecase.LandingUseCase
import com.tsarsprocket.reportmid.landingImpl.usecase.LandingUseCaseImpl
import com.tsarsprocket.reportmid.landingImpl.viewstate.LandingViewIntent
import com.tsarsprocket.reportmid.viewStateApi.view_state.ViewIntent
import dagger.Binds
import dagger.Module

@Module
internal interface LandingModule {

    @Binds
    fun bindLandingUseCase(useCase: LandingUseCaseImpl): LandingUseCase

    @Binds
    fun bindStartIntent(intent: LandingViewIntent.LandingStartLoadIntent): ViewIntent
}
