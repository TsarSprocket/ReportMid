package com.tsarsprocket.reportmid.profileScreenImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Capability
import com.tsarsprocket.reportmid.profileScreenApi.di.ProfileScreenApi
import com.tsarsprocket.reportmid.viewStateApi.di.ReducerBinding
import com.tsarsprocket.reportmid.viewStateApi.di.VisualizerBinding

@PerApi
@Capability(
    api = ProfileScreenApi::class,
    exportBindings = [
        ReducerBinding::class,
        VisualizerBinding::class,
    ]
)
internal interface ProfileScreenCapability
