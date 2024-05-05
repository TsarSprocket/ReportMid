package com.tsarsprocket.reportmid.request_manager_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base.di.AppScope
import com.tsarsprocket.reportmid.request_manager_api.di.RequestManagerApi
import dagger.Module
import dagger.Provides

@Module
interface RequestManagerProvisionModule {

    companion object {

        internal lateinit var requestManagerApiComponent: RequestManagerApiComponent

        @Provides
        @AppScope
        fun provideRequestManagerApi(appApi: AppApi): RequestManagerApi {
            return RequestManagerApiComponentLazyProxy { DaggerRequestManagerApiComponent.factory().create(appApi) }.also { requestManagerApiComponent = it }
        }
    }
}