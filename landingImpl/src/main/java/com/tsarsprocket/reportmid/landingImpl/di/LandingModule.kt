package com.tsarsprocket.reportmid.landingImpl.di

import com.tsarsprocket.reportmid.landingImpl.domain.LandingUseCase
import com.tsarsprocket.reportmid.landingImpl.domain.LandingUseCaseImpl
import com.tsarsprocket.reportmid.landingImpl.viewIntent.LandingViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import dagger.Binds
import dagger.Module

@Module
internal interface LandingModule {

    @Binds
    fun bindLandingUseCase(useCase: LandingUseCaseImpl): LandingUseCase

    @Binds
    fun bindStartIntent(intent: LandingViewIntent.LandingStartLoadIntent): ViewIntent
}
