package com.tsarsprocket.reportmid.requestManagerImpl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Capability
import com.tsarsprocket.reportmid.requestManagerApi.di.RequestManagerApi

@PerApi
@Capability(
    api = RequestManagerApi::class,
    modules = [
        RequestManagerModule::class,
    ],
    dependencies = [
        AppApi::class,
    ]
)
internal interface RequestManagerCapability