package com.tsarsprocket.reportmid.summoner_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base.di.AppScope
import com.tsarsprocket.reportmid.lol_services_api.di.LolServicesApi
import dagger.Provides

interface SummonerProvisionModule {

    companion object {

        internal lateinit var summonerApiComponent: SummonerApiComponent
            private set

        @Provides
        @AppScope
        fun provideSummonerApiComponent(lolServicesApi: LolServicesApi, appApi: AppApi): SummonerApiComponent {
            return DaggerSummonerApiComponent.factory().create(lolServicesApi, appApi).also { summonerApiComponent = it }
        }
    }
}