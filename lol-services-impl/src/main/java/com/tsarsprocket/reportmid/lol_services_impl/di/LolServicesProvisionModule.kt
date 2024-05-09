package com.tsarsprocket.reportmid.lol_services_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base_api.di.AppScope
import com.tsarsprocket.reportmid.base_api.di.BindingExport
import com.tsarsprocket.reportmid.lol_services_api.di.LolServicesApi
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