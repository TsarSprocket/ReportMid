package com.tsarsprocket.reportmid.app.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base.di.Api
import com.tsarsprocket.reportmid.base.di.ApiKey
import com.tsarsprocket.reportmid.base.di.AppScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
interface AppProvisionModule {

    @Binds
    @IntoMap
    @ApiKey(AppApi::class)
    @AppScope
    fun bindToApi(api: AppApi): Api

    companion object {

        @Provides
        @AppScope
        fun provideAppApi(): AppApi = DaggerAppApiComponent.create()
    }
}