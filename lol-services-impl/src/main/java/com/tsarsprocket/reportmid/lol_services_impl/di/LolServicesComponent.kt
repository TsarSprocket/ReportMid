package com.tsarsprocket.reportmid.lol_services_impl.di

import com.tsarsprocket.reportmid.app_api.capability.AppApi
import com.tsarsprocket.reportmid.base.capability.PerCapability
import com.tsarsprocket.reportmid.lol_services_api.capability.LolServicesApi
import dagger.Component

@PerCapability
@Component(
    modules = [
        LolServicesModule::class,
    ],
    dependencies = [
        AppApi::class,
    ]
)
interface LolServicesComponent : LolServicesApi {

    @Component.Factory
    interface Factory {
        fun create(appApi: AppApi): LolServicesComponent
    }
}