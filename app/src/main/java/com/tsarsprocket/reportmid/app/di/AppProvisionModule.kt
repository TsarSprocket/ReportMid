package com.tsarsprocket.reportmid.app.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base.di.AppScope
import com.tsarsprocket.reportmid.base.di.BindingExport
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
interface AppProvisionModule {

    @Binds
    @IntoSet
    @AppScope
    @BindingExport
    fun bindToApi(api: AppApi): Any

    companion object {

        internal lateinit var appApiComponent: AppApiComponent
            private set

        @Provides
        @AppScope
        fun provideAppApi(): AppApi {
            return AppApiComponentLazyProxy { DaggerAppApiComponent.factory().create() }.also { appApiComponent = it }
        }
    }
}