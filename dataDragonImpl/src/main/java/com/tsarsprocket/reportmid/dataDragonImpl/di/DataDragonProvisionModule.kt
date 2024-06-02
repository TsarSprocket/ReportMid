package com.tsarsprocket.reportmid.dataDragonImpl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.AppScope
import com.tsarsprocket.reportmid.dataDragonApi.di.DataDragonApi
import com.tsarsprocket.reportmid.lolServicesApi.di.LolServicesApi
import dagger.Module
import dagger.Provides

@Module
interface DataDragonProvisionModule {

    companion object {
        internal lateinit var dataDragonApiComponent: DataDragonApiComponent
            private set

        @Provides
        @AppScope
        fun provideDataDragonApiComponent(
            lolServicesApi: LolServicesApi,
            appApi: AppApi,
        ): DataDragonApi {
            return DaggerDataDragonApiComponent.factory().create(
                lolServicesApi,
                appApi,
            ).also { dataDragonApiComponent = it }
        }
    }
}