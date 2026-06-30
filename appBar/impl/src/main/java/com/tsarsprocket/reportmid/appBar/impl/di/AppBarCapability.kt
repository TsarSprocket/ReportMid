package com.tsarsprocket.reportmid.appBar.impl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.appBar.api.di.AppBarApi
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Capability

@PerApi
@Capability(
    api = AppBarApi::class,
    dependencies = [
        AppApi::class,
    ],
    modules = [
        MainModule::class,
    ],
)
internal interface AppBarCapability
