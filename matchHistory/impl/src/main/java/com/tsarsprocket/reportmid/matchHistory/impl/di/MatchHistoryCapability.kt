package com.tsarsprocket.reportmid.matchHistory.impl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.dataDragonApi.di.DataDragonApi
import com.tsarsprocket.reportmid.kspApi.annotation.Capability
import com.tsarsprocket.reportmid.lol.api.di.LolApi
import com.tsarsprocket.reportmid.matchData.api.di.MatchDataApi
import com.tsarsprocket.reportmid.matchHistory.api.di.MatchHistoryApi
import com.tsarsprocket.reportmid.navigationMapApi.di.NavigationMapApi

@PerApi
@Capability(
    api = MatchHistoryApi::class,
    dependencies = [
        AppApi::class,
        DataDragonApi::class,
        LolApi::class,
        MatchDataApi::class,
        NavigationMapApi::class,
    ],
    modules = [
        MatchHistoryMainModule::class,
    ]
)
internal interface MatchHistoryCapability
