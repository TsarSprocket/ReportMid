package com.tsarsprocket.reportmid.lol_services_impl.di

import com.tsarsprocket.reportmid.base_api.di.PerApi
import com.tsarsprocket.reportmid.lol_services_api.riotapi.ServiceFactory
import com.tsarsprocket.reportmid.lol_services_impl.riotapi.ServiceFactoryImpl
import dagger.Binds
import dagger.Module

@Module
internal interface LolServicesModule {

    @Binds
    @PerApi
    fun bindServiceFactory(factory: ServiceFactoryImpl): ServiceFactory
}