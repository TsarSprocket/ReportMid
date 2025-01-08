package com.tsarsprocket.reportmid.appImpl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.appImpl.activity.MainActivity
import com.tsarsprocket.reportmid.baseApi.di.AppScope
import com.tsarsprocket.reportmid.baseImpl.di.BaseCapabilityProvisionModule
import com.tsarsprocket.reportmid.dataDragonImpl.di.DataDragonProvisionModule
import com.tsarsprocket.reportmid.findSummonerImpl.di.FindSummonerCapabilityProvisionModule
import com.tsarsprocket.reportmid.kspProcessor.annotation.LazyProxy
import com.tsarsprocket.reportmid.landingImpl.di.LandingCapabilityProvisionModule
import com.tsarsprocket.reportmid.leaguePositionImpl.di.LeaguePositionProvisionModule
import com.tsarsprocket.reportmid.lolServicesImpl.di.LolServicesProvisionModule
import com.tsarsprocket.reportmid.navigationMapImpl.di.NavigationMapCapabilityProvisionModule
import com.tsarsprocket.reportmid.profileScreenImpl.di.ProfileScreenCapabilityProvisionModule
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
        FindSummonerCapabilityProvisionModule::class,
        LandingCapabilityProvisionModule::class,
        LeaguePositionProvisionModule::class,
        LolServicesProvisionModule::class,
        NavigationMapCapabilityProvisionModule::class,
        ProfileScreenCapabilityProvisionModule::class,
        RequestManagerCapabilityProvisionModule::class,
        StateProvisionModule::class,
        SummonerProvisionModule::class,
        ViewStateCapabilityProvisionModule::class,
    ],
)
internal interface AppApiComponent : AppApi {

    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(): AppApiComponent
    }
}