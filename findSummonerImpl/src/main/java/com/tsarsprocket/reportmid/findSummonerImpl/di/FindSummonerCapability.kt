package com.tsarsprocket.reportmid.findSummonerImpl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.dataDragonApi.di.DataDragonApi
import com.tsarsprocket.reportmid.findSummonerApi.di.FindSummonerApi
import com.tsarsprocket.reportmid.kspProcessor.annotation.Capability
import com.tsarsprocket.reportmid.navigationMapApi.di.NavigationMapApi
import com.tsarsprocket.reportmid.summonerApi.di.SummonerApi
import com.tsarsprocket.reportmid.viewStateApi.di.ReducerBinding
import com.tsarsprocket.reportmid.viewStateApi.di.VisualizerBinding

@PerApi
@Capability(
    api = FindSummonerApi::class,
    modules = [
        MainModule::class,
        ReducerModule::class,
        VisualizerModule::class,
    ],
    dependencies = [
        AppApi::class,
        DataDragonApi::class,
        NavigationMapApi::class,
        SummonerApi::class,
    ],
    exportBindings = [
        ReducerBinding::class,
        VisualizerBinding::class,
    ]
)
internal interface FindSummonerCapability
