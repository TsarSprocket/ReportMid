package com.tsarsprocket.reportmid.currentGameData.impl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.currentGameData.api.di.CurrentGameDataApi
import com.tsarsprocket.reportmid.dataDragonApi.di.DataDragonApi
import com.tsarsprocket.reportmid.kspApi.annotation.Capability
import com.tsarsprocket.reportmid.lol.api.di.LolApi
import com.tsarsprocket.reportmid.lolServicesApi.di.LolServicesApi
import com.tsarsprocket.reportmid.requestManagerApi.di.RequestManagerApi

@PerApi
@Capability(
    api = CurrentGameDataApi::class,
    dependencies = [
        DataDragonApi::class,
        LolApi::class,
        LolServicesApi::class,
        RequestManagerApi::class,
    ],
    modules = [
        CurrentGameDataMainModule::class,
    ]
)
internal interface CurrentGameDataCapability