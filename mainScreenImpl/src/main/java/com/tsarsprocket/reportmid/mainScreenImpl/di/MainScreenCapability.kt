package com.tsarsprocket.reportmid.mainScreenImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Capability
import com.tsarsprocket.reportmid.mainScreenApi.di.MainScreenApi

@PerApi
@Capability(
    api = MainScreenApi::class,
)
interface MainScreenCapability