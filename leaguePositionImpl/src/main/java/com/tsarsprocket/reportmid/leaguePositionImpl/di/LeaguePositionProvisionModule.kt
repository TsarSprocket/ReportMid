package com.tsarsprocket.reportmid.leaguePositionImpl.di

import com.tsarsprocket.reportmid.baseApi.di.AppScope
import com.tsarsprocket.reportmid.leaguePositionApi.di.LeaguePositionApi
import com.tsarsprocket.reportmid.lolServicesApi.di.LolServicesApi
import dagger.Module
import dagger.Provides

@Module
interface LeaguePositionProvisionModule {

    companion object {

        internal lateinit var leaguePositionApiComponent: LeaguePositionApiComponent
            private set

        @Provides
        @AppScope
        fun provideLeaguePositionApi(lolServicesApi: LolServicesApi): LeaguePositionApi {
            return DaggerLeaguePositionApiComponent.factory().create(lolServicesApi).also { leaguePositionApiComponent = it }
        }
    }
}