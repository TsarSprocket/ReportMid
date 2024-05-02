package com.tsarsprocket.reportmid.app_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.app_api.di.AppApiLazyProxy
import com.tsarsprocket.reportmid.base.di.AppScope
import dagger.Module
import dagger.Provides

@Module
internal interface AppProvisioningModule {

    companion object {

        internal lateinit var appApiComponent: AppApiComponent

        @Provides
        @AppScope
        fun provideAppApi(): AppApi = AppApiLazyProxy { appApiComponent }
    }
}