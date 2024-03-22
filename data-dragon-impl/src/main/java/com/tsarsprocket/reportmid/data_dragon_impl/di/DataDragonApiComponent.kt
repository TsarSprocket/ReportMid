package com.tsarsprocket.reportmid.data_dragon_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base.di.PerApi
import com.tsarsprocket.reportmid.data_dragon_api.di.DataDragonApi
import com.tsarsprocket.reportmid.lol_services_api.di.LolServicesApi
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