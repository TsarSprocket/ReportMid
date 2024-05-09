package com.tsarsprocket.reportmid.request_manager_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base_api.di.PerApi
import com.tsarsprocket.reportmid.ksp_processor.annotation.LazyProxy
import com.tsarsprocket.reportmid.request_manager_api.di.RequestManagerApi
import dagger.Component

@LazyProxy
@PerApi
@Component(
    modules = [
        RequestManagerModule::class,
    ],
    dependencies = [
        AppApi::class,
    ]
)
internal interface RequestManagerApiComponent : RequestManagerApi {

    @Component.Factory
    interface Factrory {

        fun create(appApi: AppApi): RequestManagerApiComponent
    }
}