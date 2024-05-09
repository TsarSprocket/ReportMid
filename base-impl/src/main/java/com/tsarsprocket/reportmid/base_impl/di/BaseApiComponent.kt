package com.tsarsprocket.reportmid.base_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base_api.di.BaseApi
import com.tsarsprocket.reportmid.base_api.di.PerApi
import com.tsarsprocket.reportmid.ksp_processor.annotation.LazyProxy
import dagger.Component

@LazyProxy
@PerApi
@Component(
    modules = [
        BaseModule::class,
    ],
    dependencies = [
        AppApi::class,
    ]
)
internal interface BaseApiComponent : BaseApi {

    @Component.Factory
    interface Factory {
        fun create(appApi: AppApi): BaseApiComponent
    }
}