package com.tsarsprocket.reportmid.landingImpl.di

import com.tsarsprocket.reportmid.landingApi.viewIntent.LandingViewIntent
import com.tsarsprocket.reportmid.landingImpl.domain.LandingUseCase
import com.tsarsprocket.reportmid.landingImpl.domain.LandingUseCaseImpl
import com.tsarsprocket.reportmid.landingImpl.viewIntent.LandingViewIntentImpl.LandingStartLoadIntent
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
internal interface LandingModule {

    @Binds
    fun bindLandingUseCase(useCase: LandingUseCaseImpl): LandingUseCase

    companion object {

        @Provides
        fun bindLandingViewIntentCreator(): () -> LandingViewIntent = { LandingStartLoadIntent }
    }
}
