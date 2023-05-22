package com.tsarsprocket.reportmid.landing.capability

import com.tsarsprocket.reportmid.base.capability.Capability
import com.tsarsprocket.reportmid.landing.view.LandingFragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
interface LandingCapabilityModule {

    @Binds
    @IntoSet
    fun bindLandingToCapability(landing: Landing): Capability<*>

    companion object {

        @Provides
        @Singleton
        fun provideLandingApi(landing: Landing): LandingApi = landing.api
    }
}