package com.tsarsprocket.reportmid.navigationMapImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspProcessor.annotation.Capability
import com.tsarsprocket.reportmid.navigationMapApi.di.NavigationMapApi

@PerApi
@Capability(
    api = NavigationMapApi::class,
    modules = [
        FindSummonerNavigationModule::class,
        LandingNavigationModule::class,
        NavigationMapModule::class,
    ],
)
internal interface NavigationMapCapability