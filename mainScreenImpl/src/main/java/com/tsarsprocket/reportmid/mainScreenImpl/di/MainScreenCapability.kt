package com.tsarsprocket.reportmid.mainScreenImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Capability
import com.tsarsprocket.reportmid.mainScreenApi.di.MainScreenApi
import com.tsarsprocket.reportmid.mainScreenApi.navigation.MainScreenNavigation
import com.tsarsprocket.reportmid.mainScreenImpl.viewState.MainScreenViewState
import com.tsarsprocket.reportmid.navigationMapApi.di.NavigationMapApi
import com.tsarsprocket.reportmid.viewStateApi.navigation.Navigation

@PerApi
@Capability(
    api = MainScreenApi::class,
    dependencies = [
        NavigationMapApi::class,
    ],
)
interface MainScreenCapability {
    @Navigation(MainScreenNavigation.TAG)
    fun getMainScreenNavigation(): MainScreenNavigation
    fun inject(state: MainScreenViewState)
}