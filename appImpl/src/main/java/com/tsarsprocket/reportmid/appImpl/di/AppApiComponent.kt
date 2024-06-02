package com.tsarsprocket.reportmid.appImpl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.AppScope
import com.tsarsprocket.reportmid.baseImpl.di.BaseCapabilityProvisionModule
import com.tsarsprocket.reportmid.dataDragonImpl.di.DataDragonProvisionModule
import com.tsarsprocket.reportmid.kspProcessor.annotation.LazyProxy
import com.tsarsprocket.reportmid.landingImpl.di.LandingProvisionModule
import com.tsarsprocket.reportmid.leaguePositionImpl.di.LeaguePositionProvisionModule
import com.tsarsprocket.reportmid.lolServicesImpl.di.LolServicesProvisionModule
import com.tsarsprocket.reportmid.requestManagerImpl.di.RequestManagerCapabilityProvisionModule
import com.tsarsprocket.reportmid.stateImpl.di.StateProvisionModule
import com.tsarsprocket.reportmid.summonerImpl.di.SummonerProvisionModule
import com.tsarsprocket.reportmid.viewStateImpl.di.ViewStateCapabilityProvisionModule
import dagger.Component

@LazyProxy
@AppScope
@Component(
    modules = [
        AppModule::class,
        AggregatorModule::class,
        // Provision modules
        AppProvisionModule::class,
        BaseCapabilityProvisionModule::class,
        DataDragonProvisionModule::class,
        LandingProvisionModule::class,
        LeaguePositionProvisionModule::class,
        LolServicesProvisionModule::class,
        RequestManagerCapabilityProvisionModule::class,
        StateProvisionModule::class,
        SummonerProvisionModule::class,
        ViewStateCapabilityProvisionModule::class,
    ],
)
internal interface AppApiComponent : AppApi {

    @Component.Factory
    interface Factory {
        fun create(): AppApiComponent
    }
}