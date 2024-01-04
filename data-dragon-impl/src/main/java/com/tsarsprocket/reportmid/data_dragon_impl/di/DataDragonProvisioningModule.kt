package com.tsarsprocket.reportmid.data_dragon_impl.di

import com.tsarsprocket.reportmid.lol_services_api.di.LolServicesApi
import dagger.Module

@Module
interface DataDragonProvisioningModule {

    companion object {
        internal lateinit var dataDragonApiComponent: DataDragonApiComponent
            private set

        fun provideDataDragonApiComponent(lolServicesApi: LolServicesApi): DataDragonApiComponent {
            return DaggerDataDragonApiComponent.factory().create(lolServicesApi).also { dataDragonApiComponent = it }
        }
    }
}