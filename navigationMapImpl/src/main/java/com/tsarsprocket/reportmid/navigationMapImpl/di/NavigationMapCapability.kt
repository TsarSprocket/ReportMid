package com.tsarsprocket.reportmid.navigationMapImpl.di

import com.tsarsprocket.reportmid.kspProcessor.annotation.Capability
import com.tsarsprocket.reportmid.navigationMapApi.di.NavigationMapApi
import com.tsarsprocket.reportmid.profileScreenApi.di.ProfileScreenApi

@Capability(
    api = NavigationMapApi::class,
    modules = [
        NavigationMapModule::class,
    ],
    dependencies = [
        ProfileScreenApi::class,
    ],
)
internal interface NavigationMapCapability