package com.tsarsprocket.reportmid.data_dragon_impl.di

import dagger.Module

@Module
interface DataDragonProvisioningModule {

    companion object {
        internal lateinit var dataDragonApiComponent: DataDragonApiComponent
            private set

        fun provideDataDragonApiComponent(): DataDragonApiComponent {
            return DaggerDataDragonApiComponent.create().also { dataDragonApiComponent = it }
        }
    }
}