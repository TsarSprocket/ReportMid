package com.tsarsprocket.reportmid.app.capability

import com.tsarsprocket.reportmid.app_api.capability.AppApi
import com.tsarsprocket.reportmid.base.capability.Capability
import com.tsarsprocket.reportmid.base.di.AppScope
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface AppCapabilityModule {

    @Binds
    @AppScope
    fun bindAppCapabilityToCapability(appCapability: AppCapability): Capability<*>

    companion object {

        @Provides
        @AppScope
        fun provideAppCapability() = AppCapability()

        @Provides
        @AppScope
        fun provideAppApi(appCapability: AppCapability): AppApi = appCapability.api
    }
}
