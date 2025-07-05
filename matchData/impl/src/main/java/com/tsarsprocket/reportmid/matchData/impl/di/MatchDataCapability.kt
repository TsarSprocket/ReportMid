package com.tsarsprocket.reportmid.matchData.impl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.dataDragonApi.di.DataDragonApi
import com.tsarsprocket.reportmid.kspApi.annotation.Capability
import com.tsarsprocket.reportmid.lol.api.di.LolApi
import com.tsarsprocket.reportmid.lolServicesApi.di.LolServicesApi
import com.tsarsprocket.reportmid.matchData.api.di.MatchDataApi
import com.tsarsprocket.reportmid.requestManagerApi.di.RequestManagerApi

@PerApi
@Capability(
    api = MatchDataApi::class,
    dependencies = [
        AppApi::class,
        DataDragonApi::class,
        LolApi::class,
        LolServicesApi::class,
        RequestManagerApi::class,
    ],
    modules = [
        MatchDataModule::class,
    ]
)
internal interface MatchDataCapability
