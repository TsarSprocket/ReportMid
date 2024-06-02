package com.tsarsprocket.reportmid.lolServicesImpl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.AppScope
import com.tsarsprocket.reportmid.baseApi.di.BindingExport
import com.tsarsprocket.reportmid.lolServicesApi.di.LolServicesApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Provider

@Module
interface LolServicesProvisionModule {

    @Binds
    @IntoSet
    @AppScope
    @BindingExport
    fun bindToApi(api: LolServicesApi): Any

    companion object {

        internal lateinit var lolServicesApiComponent: LolServicesApiComponent
            private set

        @Provides
        @AppScope
        fun provideLolServicesApi(appApi: Provider<AppApi>): LolServicesApi {
            return DaggerLolServicesApiComponent.factory().create(appApi.get()).also { lolServicesApiComponent = it }
        }
    }
}