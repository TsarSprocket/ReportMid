package com.tsarsprocket.reportmid.lol_services_impl.di

import com.tsarsprocket.reportmid.base.capability.PerCapability
import com.tsarsprocket.reportmid.lol_services_api.riotapi.ServiceFactory
import com.tsarsprocket.reportmid.lol_services_impl.riotapi.ServiceFactoryImpl
import dagger.Binds
import dagger.Module

@Module
interface LolServicesModule {

    @Binds
    @PerCapability
    fun bindServiceFactory(factory: ServiceFactoryImpl): ServiceFactory
}