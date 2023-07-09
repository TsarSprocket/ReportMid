package com.tsarsprocket.reportmid.landing.capability

import com.tsarsprocket.reportmid.base.capability.Capability
import com.tsarsprocket.reportmid.base.di.AppScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
interface LandingCapabilityModule {

    @Binds
    @AppScope
    @IntoSet
    fun bindLandingCapabilityToCapability(landingCapability: LandingCapability): Capability<*>

    companion object {

        @Provides
        @AppScope
        fun provideLandingCapability() = LandingCapability()

        @Provides
        @AppScope
        fun provideLandingApi(landingCapability: LandingCapability): LandingApi = landingCapability.api
    }
}