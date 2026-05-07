package com.tsarsprocket.reportmid.currentGameData.impl.di

import com.tsarsprocket.reportmid.currentGameData.api.di.CurrentGameDataApi
import com.tsarsprocket.reportmid.dataDragonApi.di.DataDragonApi
import com.tsarsprocket.reportmid.kspApi.annotation.Capability
import com.tsarsprocket.reportmid.requestManagerApi.di.RequestManagerApi

@Capability(
    api = CurrentGameDataApi::class,
    dependencies = [
        DataDragonApi::class,
        RequestManagerApi::class,
    ],
    modules = [
        CurrentGameDataMainModule::class,
    ]
)
internal interface CurrentGameDataCapability