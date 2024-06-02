package com.tsarsprocket.reportmid.summonerImpl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.lolServicesApi.di.LolServicesApi
import com.tsarsprocket.reportmid.requestManagerApi.di.RequestManagerApi
import com.tsarsprocket.reportmid.summonerApi.di.SummonerApi
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