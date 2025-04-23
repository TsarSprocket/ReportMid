package com.tsarsprocket.reportmid.mainScreenImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Capability
import com.tsarsprocket.reportmid.mainScreenApi.di.MainScreenApi
import com.tsarsprocket.reportmid.navigationMapApi.di.NavigationMapApi
import com.tsarsprocket.reportmid.viewStateApi.di.ViewStateInitializerBinding

@PerApi
@Capability(
    api = MainScreenApi::class,
    exportBindings = [
        ViewStateInitializerBinding::class,
    ],
    modules = [
        MainScreenStateInitializerBindingModule::class,
    ],
    dependencies = [
        NavigationMapApi::class,
    ],
)
interface MainScreenCapability