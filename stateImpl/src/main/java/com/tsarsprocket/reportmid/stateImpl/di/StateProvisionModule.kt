package com.tsarsprocket.reportmid.stateImpl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.AppScope
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.BindingExport
import com.tsarsprocket.reportmid.stateApi.di.StateApi
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