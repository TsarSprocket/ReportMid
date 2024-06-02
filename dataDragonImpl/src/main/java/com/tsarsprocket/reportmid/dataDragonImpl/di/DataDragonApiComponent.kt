package com.tsarsprocket.reportmid.dataDragonImpl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.dataDragonApi.di.DataDragonApi
import com.tsarsprocket.reportmid.lolServicesApi.di.LolServicesApi
import dagger.Component

@PerApi
@Component(
    modules = [
        DataDragonModule::class,
    ],
    dependencies = [
        LolServicesApi::class,
        AppApi::class,
    ]
)
internal interface DataDragonApiComponent : DataDragonApi {

    @Component.Factory
    interface Factory {
        fun create(
            lolServicesApi: LolServicesApi,
            appApi: AppApi,
        ): DataDragonApiComponent
    }
}