package com.tsarsprocket.reportmid.profileOverviewImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.dataDragonApi.di.DataDragonApi
import com.tsarsprocket.reportmid.kspApi.annotation.Capability
import com.tsarsprocket.reportmid.profileOverviewApi.di.ProfileOverviewApi
import com.tsarsprocket.reportmid.summonerApi.di.SummonerApi
import com.tsarsprocket.reportmid.viewStateApi.di.ReducerBinding
import com.tsarsprocket.reportmid.viewStateApi.di.VisualizerBinding

@PerApi
@Capability(
    api = ProfileOverviewApi::class,
    exportBindings = [
        ReducerBinding::class,
        VisualizerBinding::class,
    ],
    dependencies = [
        DataDragonApi::class,
        SummonerApi::class,
    ],
    modules = [
        ProfileOverviewMainModule::class,
    ]
)
internal interface ProfileOverviewCapability
