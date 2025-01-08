package com.tsarsprocket.reportmid.profileScreenImpl.di

import com.tsarsprocket.reportmid.kspProcessor.annotation.Capability
import com.tsarsprocket.reportmid.profileScreenApi.di.ProfileScreenApi

@Capability(
    api = ProfileScreenApi::class,
    modules = [
        ProfileScreenModule::class,
    ],
)
internal interface ProfileScreenCapability {
}