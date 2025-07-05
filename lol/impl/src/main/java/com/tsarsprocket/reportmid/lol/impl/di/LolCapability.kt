package com.tsarsprocket.reportmid.lol.impl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Capability
import com.tsarsprocket.reportmid.lol.api.di.LolApi

@PerApi
@Capability(
    api = LolApi::class,
    modules = [
        LolMainModule::class,
    ],
    dependencies = [
        AppApi::class,
    ],
)
interface LolCapability