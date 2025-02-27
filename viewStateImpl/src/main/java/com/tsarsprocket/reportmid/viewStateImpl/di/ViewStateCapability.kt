package com.tsarsprocket.reportmid.viewStateImpl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.FragmentsCreator
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Capability
import com.tsarsprocket.reportmid.viewStateApi.di.EffectHandlerBinding
import com.tsarsprocket.reportmid.viewStateApi.di.ReducerBinding
import com.tsarsprocket.reportmid.viewStateApi.di.ViewStateApi
import com.tsarsprocket.reportmid.viewStateApi.di.VisualizerBinding

@PerApi
@Capability(
    api = ViewStateApi::class,
    modules = [
        EffectHandlerModule::class,
        MainModule::class,
        ReducerModule::class,
        VisualizerModule::class,
    ],
    dependencies = [
        AppApi::class,
    ],
    exportBindings = [
        EffectHandlerBinding::class,
        FragmentsCreator::class,
        ReducerBinding::class,
        VisualizerBinding::class,
    ]
)
internal interface ViewStateCapability