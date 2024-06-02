package com.tsarsprocket.reportmid.lolServicesImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.lolServicesApi.riotapi.ServiceFactory
import com.tsarsprocket.reportmid.lolServicesImpl.riotapi.ServiceFactoryImpl
import dagger.Binds
import dagger.Module

@Module
internal interface LolServicesModule {

    @Binds
    @PerApi
    fun bindServiceFactory(factory: ServiceFactoryImpl): ServiceFactory
}