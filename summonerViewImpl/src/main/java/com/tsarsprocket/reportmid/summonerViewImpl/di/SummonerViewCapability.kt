package com.tsarsprocket.reportmid.summonerViewImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.dataDragonApi.di.DataDragonApi
import com.tsarsprocket.reportmid.kspApi.annotation.Capability
import com.tsarsprocket.reportmid.navigationMapApi.di.NavigationMapApi
import com.tsarsprocket.reportmid.summonerApi.di.SummonerApi
import com.tsarsprocket.reportmid.summonerViewApi.di.SummonerViewApi

@PerApi
@Capability(
    api = SummonerViewApi::class,
    dependencies = [
        DataDragonApi::class,
        NavigationMapApi::class,
        SummonerApi::class,
    ],
)
internal interface SummonerViewCapability
