package com.tsarsprocket.reportmid.landingImpl.di

import com.tsarsprocket.reportmid.landingImpl.domain.LandingUseCase
import com.tsarsprocket.reportmid.landingImpl.domain.LandingUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
internal interface MainModule {

    @Binds
    fun bindLandingUseCase(useCase: LandingUseCaseImpl): LandingUseCase
}
