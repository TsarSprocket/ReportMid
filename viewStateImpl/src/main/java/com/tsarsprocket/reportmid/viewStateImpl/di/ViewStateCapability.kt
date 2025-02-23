package com.tsarsprocket.reportmid.viewStateImpl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.FragmentsCreator
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspProcessor.annotation.Capability
import com.tsarsprocket.reportmid.viewStateApi.di.ReducerBinding
import com.tsarsprocket.reportmid.viewStateApi.di.ViewStateApi
import com.tsarsprocket.reportmid.viewStateApi.di.VisualizerBinding

@PerApi
@Capability(
    api = ViewStateApi::class,
    modules = [
        MainModule::class,
        ReducerModule::class,
        VisualizerModule::class,
    ],
    dependencies = [
        AppApi::class,
    ],
    exportBindings = [
        FragmentsCreator::class,
        ReducerBinding::class,
        VisualizerBinding::class,
    ]
)
internal interface ViewStateCapability