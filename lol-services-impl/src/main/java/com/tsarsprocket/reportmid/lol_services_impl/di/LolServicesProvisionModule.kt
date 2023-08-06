package com.tsarsprocket.reportmid.lol_services_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base.di.Api
import com.tsarsprocket.reportmid.base.di.AppScope
import com.tsarsprocket.reportmid.lol_services_api.di.LolServicesApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import javax.inject.Provider

@Module
interface LolServicesProvisionModule {

    @Binds
    @IntoMap
    @ClassKey(LolServicesApi::class)
    fun bindToApi(api: LolServicesApi): Api

    companion object {

        @Provides
        @AppScope
        fun provideLolServicesApi(appApi: Provider<AppApi>): LolServicesApi = DaggerLolServicesComponent.factory().create(appApi.get())
    }
}