package com.tsarsprocket.reportmid.landingImpl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.dataDragonApi.di.DataDragonApi
import com.tsarsprocket.reportmid.findSummonerApi.di.FindSummonerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Capability
import com.tsarsprocket.reportmid.landingApi.di.LandingApi
import com.tsarsprocket.reportmid.navigationMapApi.di.NavigationMapApi
import com.tsarsprocket.reportmid.stateApi.di.StateApi
import com.tsarsprocket.reportmid.summonerApi.di.SummonerApi
import com.tsarsprocket.reportmid.viewStateApi.di.ReducerBinding
import com.tsarsprocket.reportmid.viewStateApi.di.VisualizerBinding

@PerApi
@Capability(
    api = LandingApi::class,
    dependencies = [
        AppApi::class,
        DataDragonApi::class,
        FindSummonerApi::class,
        NavigationMapApi::class,
        StateApi::class,
        SummonerApi::class,
    ],
    modules = [
        MainModule::class,
        VisualizerModule::class,
    ],
    exportBindings = [
        ReducerBinding::class,
        VisualizerBinding::class,
    ]
)
internal interface LandingCapability
