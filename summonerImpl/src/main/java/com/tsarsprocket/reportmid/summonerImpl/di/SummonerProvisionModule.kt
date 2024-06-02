package com.tsarsprocket.reportmid.summonerImpl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.AppScope
import com.tsarsprocket.reportmid.lolServicesApi.di.LolServicesApi
import com.tsarsprocket.reportmid.requestManagerApi.di.RequestManagerApi
import com.tsarsprocket.reportmid.summonerApi.di.SummonerApi
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