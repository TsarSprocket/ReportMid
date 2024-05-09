package com.tsarsprocket.reportmid.lol_services_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base_api.di.PerApi
import com.tsarsprocket.reportmid.lol_services_api.di.LolServicesApi
import dagger.Component

@PerApi
@Component(
    modules = [
        LolServicesModule::class,
    ],
    dependencies = [
        AppApi::class,
    ]
)
internal interface LolServicesApiComponent : LolServicesApi {

    @Component.Factory
    interface Factory {
        fun create(appApi: AppApi): LolServicesApiComponent
    }
}