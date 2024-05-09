package com.tsarsprocket.reportmid.view_state_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base_api.di.AppScope
import com.tsarsprocket.reportmid.base_api.di.BindingExport
import com.tsarsprocket.reportmid.view_state_api.di.ViewStateApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Provider

@Module
interface ViewStateProvisionModule {

    @Binds
    @IntoSet
    @AppScope
    @BindingExport
    fun bindBindingExports(api: ViewStateApi): Any

    companion object {

        internal lateinit var viewStateApiComponent: ViewStateApiComponent
            private set

        @Provides
        @AppScope
        fun provideViewStateApi(appApi: Provider<AppApi>): ViewStateApi {
            return DaggerViewStateApiComponent.factory().create(appApi.get()).also { viewStateApiComponent = it }
        }
    }
}