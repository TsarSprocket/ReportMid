package com.tsarsprocket.reportmid.view_state_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base_api.di.FragmentsCreator
import com.tsarsprocket.reportmid.base_api.di.PerApi
import com.tsarsprocket.reportmid.ksp_processor.annotation.Capability
import com.tsarsprocket.reportmid.view_state_api.di.ViewStateApi

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