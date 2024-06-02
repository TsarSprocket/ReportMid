package com.tsarsprocket.reportmid.viewStateImpl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.FragmentsCreator
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspProcessor.annotation.Capability
import com.tsarsprocket.reportmid.viewStateApi.di.ViewStateApi

@PerApi
@Capability(
    api = ViewStateApi::class,
    modules = [
        ViewStateModule::class,
    ],
    dependencies = [
        AppApi::class,
    ],
    exportBindings = [
        FragmentsCreator::class,
    ]
)
internal interface ViewStateCapability