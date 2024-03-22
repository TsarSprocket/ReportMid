package com.tsarsprocket.reportmid.state_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base.di.AppScope
import com.tsarsprocket.reportmid.base.di.BindingExport
import com.tsarsprocket.reportmid.state_api.di.StateApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
interface StateProvisionModule {

    @Binds
    @IntoSet
    @AppScope
    @BindingExport
    fun binStateApi(api: StateApi): Any

    companion object {

        internal lateinit var stateApiComponent: StateApiComponent
            private set

        @Provides
        @AppScope
        fun provideStateApi(appApi: AppApi): StateApi = DaggerStateApiComponent.factory().create(appApi).also { stateApiComponent = it }
    }
}