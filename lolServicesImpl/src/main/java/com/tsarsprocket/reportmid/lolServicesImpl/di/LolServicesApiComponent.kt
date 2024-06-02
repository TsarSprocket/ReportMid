package com.tsarsprocket.reportmid.lolServicesImpl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.lolServicesApi.di.LolServicesApi
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