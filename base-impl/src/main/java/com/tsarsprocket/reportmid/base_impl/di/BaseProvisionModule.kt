package com.tsarsprocket.reportmid.base_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base_api.di.AppScope
import com.tsarsprocket.reportmid.base_api.di.BaseApi
import dagger.Module
import dagger.Provides

@Module
interface BaseProvisionModule {

    companion object {

        internal lateinit var baseApiComponent: BaseApiComponent

        @Provides
        @AppScope
        fun provideBaseApi(appApi: AppApi): BaseApi {
            return BaseApiComponentLazyProxy { DaggerBaseApiComponent.factory().create(appApi) }.also { baseApiComponent = it }
        }
    }
}