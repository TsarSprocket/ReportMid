package com.tsarsprocket.reportmid.league_position_impl.di

import com.tsarsprocket.reportmid.base_api.di.AppScope
import com.tsarsprocket.reportmid.league_position_api.di.LeaguePositionApi
import com.tsarsprocket.reportmid.lol_services_api.di.LolServicesApi
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