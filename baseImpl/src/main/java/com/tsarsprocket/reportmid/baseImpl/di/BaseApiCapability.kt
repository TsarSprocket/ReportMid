package com.tsarsprocket.reportmid.baseImpl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.BaseApi
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Capability

@PerApi
@Capability(
    api = BaseApi::class,
    modules = [
        BaseModule::class,
    ],
    dependencies = [
        AppApi::class,
    ]
)
internal interface BaseCapability