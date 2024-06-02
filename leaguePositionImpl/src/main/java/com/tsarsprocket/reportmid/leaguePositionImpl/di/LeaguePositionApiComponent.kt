package com.tsarsprocket.reportmid.leaguePositionImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.leaguePositionApi.di.LeaguePositionApi
import com.tsarsprocket.reportmid.lolServicesApi.di.LolServicesApi
import dagger.Component

@PerApi
@Component(
    modules = [
        LeaguePositionModule::class,
    ],
    dependencies = [
        LolServicesApi::class,
    ]
)
interface LeaguePositionApiComponent : LeaguePositionApi {

    @Component.Factory
    interface Factory {
        fun create(lolServicesApi: LolServicesApi): LeaguePositionApiComponent
    }
}