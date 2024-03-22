package com.tsarsprocket.reportmid.data_dragon_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base.di.AppScope
import com.tsarsprocket.reportmid.data_dragon_api.di.DataDragonApi
import com.tsarsprocket.reportmid.lol_services_api.di.LolServicesApi
import dagger.Module
import dagger.Provides

@Module
interface DataDragonProvisioningModule {

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