package com.tsarsprocket.reportmid.navigationMapImpl.di

import com.tsarsprocket.reportmid.kspProcessor.annotation.Capability
import com.tsarsprocket.reportmid.navigationMapApi.di.NavigationMapApi

@Capability(
    api = NavigationMapApi::class,
    modules = [
        NavigationMapModule::class,
    ],
)
internal interface NavigationMapCapability