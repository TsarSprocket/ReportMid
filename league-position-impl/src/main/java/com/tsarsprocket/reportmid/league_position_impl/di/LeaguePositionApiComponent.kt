package com.tsarsprocket.reportmid.league_position_impl.di

import com.tsarsprocket.reportmid.base_api.di.PerApi
import com.tsarsprocket.reportmid.league_position_api.di.LeaguePositionApi
import com.tsarsprocket.reportmid.lol_services_api.di.LolServicesApi
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