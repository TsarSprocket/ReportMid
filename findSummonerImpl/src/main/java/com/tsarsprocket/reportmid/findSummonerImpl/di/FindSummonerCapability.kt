package com.tsarsprocket.reportmid.findSummonerImpl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.dataDragonApi.di.DataDragonApi
import com.tsarsprocket.reportmid.findSummonerApi.di.FindSummonerApi
import com.tsarsprocket.reportmid.findSummonerImpl.viewIntent.FindAndConfirmSummonerViewIntent
import com.tsarsprocket.reportmid.kspProcessor.annotation.Capability
import com.tsarsprocket.reportmid.summonerApi.di.SummonerApi

@PerApi
@Capability(
    api = FindSummonerApi::class,
    modules = [
        FindSummonerModule::class,
    ],
    dependencies = [
        AppApi::class,
        SummonerApi::class,
        DataDragonApi::class,
    ]
)
internal interface FindSummonerCapability {
    fun inject(viewIntent: FindAndConfirmSummonerViewIntent)
}
