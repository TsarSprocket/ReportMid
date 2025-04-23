package com.tsarsprocket.reportmid.viewStateImpl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.BaseApi
import com.tsarsprocket.reportmid.baseApi.di.FragmentCreatorBinding
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.baseApi.di.ViewModelFactoryCreatorBinding
import com.tsarsprocket.reportmid.kspApi.annotation.Capability
import com.tsarsprocket.reportmid.viewStateApi.di.EffectHandlerBinding
import com.tsarsprocket.reportmid.viewStateApi.di.ReducerBinding
import com.tsarsprocket.reportmid.viewStateApi.di.ViewStateApi
import com.tsarsprocket.reportmid.viewStateApi.di.VisualizerBinding
import com.tsarsprocket.reportmid.viewStateImpl.viewmodel.ViewStateHolderImpl

@PerApi
@Capability(
    api = ViewStateApi::class,
    modules = [
        ViewStateFragmentModule::class,
        ViewStateViewModelModule::class,
    ],
    dependencies = [
        AppApi::class,
        BaseApi::class,
    ],
    exportBindings = [
        EffectHandlerBinding::class,
        FragmentCreatorBinding::class,
        ReducerBinding::class,
        ViewModelFactoryCreatorBinding::class,
        VisualizerBinding::class,
    ]
)
internal interface ViewStateCapability {
    fun inject(holder: ViewStateHolderImpl)
}