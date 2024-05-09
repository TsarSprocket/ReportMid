package com.tsarsprocket.reportmid.app_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base_api.di.AppScope
import com.tsarsprocket.reportmid.base_impl.di.BaseProvisionModule
import com.tsarsprocket.reportmid.data_dragon_impl.di.DataDragonProvisionModule
import com.tsarsprocket.reportmid.ksp_processor.annotation.LazyProxy
import com.tsarsprocket.reportmid.landing_impl.di.LandingProvisionModule
import com.tsarsprocket.reportmid.league_position_impl.di.LeaguePositionProvisionModule
import com.tsarsprocket.reportmid.lol_services_impl.di.LolServicesProvisionModule
import com.tsarsprocket.reportmid.request_manager_impl.di.RequestManagerProvisionModule
import com.tsarsprocket.reportmid.state_impl.di.StateProvisionModule
import com.tsarsprocket.reportmid.summoner_impl.di.SummonerProvisionModule
import com.tsarsprocket.reportmid.view_state_impl.di.ViewStateProvisionModule
import dagger.Component

@LazyProxy
@AppScope
@Component(
    modules = [
        AppModule::class,
        AggregatorModule::class,
        // Provision modules
        AppProvisionModule::class,
        BaseProvisionModule::class,
        DataDragonProvisionModule::class,
        LandingProvisionModule::class,
        LeaguePositionProvisionModule::class,
        LolServicesProvisionModule::class,
        RequestManagerProvisionModule::class,
        StateProvisionModule::class,
        SummonerProvisionModule::class,
        ViewStateProvisionModule::class,
    ],
)
internal interface AppApiComponent : AppApi {

    @Component.Factory
    interface Factory {
        fun create(): AppApiComponent
    }
}