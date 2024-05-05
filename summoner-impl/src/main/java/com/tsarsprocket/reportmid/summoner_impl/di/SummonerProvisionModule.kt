package com.tsarsprocket.reportmid.summoner_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base.di.AppScope
import com.tsarsprocket.reportmid.lol_services_api.di.LolServicesApi
import com.tsarsprocket.reportmid.request_manager_api.di.RequestManagerApi
import com.tsarsprocket.reportmid.summoner_api.di.SummonerApi
import dagger.Module
import dagger.Provides

@Module
interface SummonerProvisionModule {

    companion object {

        internal lateinit var summonerApiComponent: SummonerApiComponent
            private set

        @Provides
        @AppScope
        fun provideSummonerApi(lolServicesApi: LolServicesApi, appApi: AppApi, requestManagerApi: RequestManagerApi): SummonerApi {
            return DaggerSummonerApiComponent.factory().create(lolServicesApi, appApi, requestManagerApi).also { summonerApiComponent = it }
        }
    }
}