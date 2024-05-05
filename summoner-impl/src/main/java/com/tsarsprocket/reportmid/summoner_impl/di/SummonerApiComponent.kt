package com.tsarsprocket.reportmid.summoner_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base.di.PerApi
import com.tsarsprocket.reportmid.lol_services_api.di.LolServicesApi
import com.tsarsprocket.reportmid.request_manager_api.di.RequestManagerApi
import com.tsarsprocket.reportmid.summoner_api.di.SummonerApi
import dagger.Component

@PerApi
@Component(
    modules = [
        SummonerModule::class,
    ],
    dependencies = [
        LolServicesApi::class,
        AppApi::class,
        RequestManagerApi::class,
    ]
)
interface SummonerApiComponent : SummonerApi {

    @Component.Factory
    interface Factory {

        fun create(lolServicesApi: LolServicesApi, appApi: AppApi, requestManagerApi: RequestManagerApi): SummonerApiComponent
    }
}