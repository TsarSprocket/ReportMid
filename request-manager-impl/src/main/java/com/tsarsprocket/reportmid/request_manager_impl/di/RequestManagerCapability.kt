package com.tsarsprocket.reportmid.request_manager_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base_api.di.PerApi
import com.tsarsprocket.reportmid.ksp_processor.annotation.Capability
import com.tsarsprocket.reportmid.request_manager_api.di.RequestManagerApi

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