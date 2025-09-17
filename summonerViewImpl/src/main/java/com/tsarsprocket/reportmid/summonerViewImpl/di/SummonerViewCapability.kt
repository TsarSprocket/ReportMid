package com.tsarsprocket.reportmid.summonerViewImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Capability
import com.tsarsprocket.reportmid.navigationMapApi.di.NavigationMapApi
import com.tsarsprocket.reportmid.summonerViewApi.di.SummonerViewApi
import com.tsarsprocket.reportmid.viewStateApi.di.ViewStateInitializerBinding

@PerApi
@Capability(
    api = SummonerViewApi::class,
    exportBindings = [
        ViewStateInitializerBinding::class,
    ],
    modules = [
        SummonerViewStateInitializerBindingModule::class,
    ],
    dependencies = [
        NavigationMapApi::class,
    ],
)
internal interface SummonerViewCapability
