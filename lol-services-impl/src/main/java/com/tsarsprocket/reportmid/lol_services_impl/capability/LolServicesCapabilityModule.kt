package com.tsarsprocket.reportmid.lol_services_impl.capability

import com.tsarsprocket.reportmid.app_api.capability.AppApi
import com.tsarsprocket.reportmid.base.capability.Capability
import com.tsarsprocket.reportmid.base.di.AppScope
import com.tsarsprocket.reportmid.lol_services_api.capability.LolServicesApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Provider

@Module
interface LolServicesCapabilityModule {

    @Binds
    @AppScope
    @IntoSet
    fun bindLolServicesCapability(lolServicesCapability: LolServicesCapability): Capability<*>

    companion object {

        @Provides
        @AppScope
        fun provideLolServicesCapability(appApi: Provider<AppApi>) = LolServicesCapability(appApi)

        @Provides
        @AppScope
        fun provideLolServicesApi(lolServicesCapability: LolServicesCapability): LolServicesApi = lolServicesCapability.api
    }
}