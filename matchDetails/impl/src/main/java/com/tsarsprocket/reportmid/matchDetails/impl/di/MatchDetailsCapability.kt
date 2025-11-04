package com.tsarsprocket.reportmid.matchDetails.impl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.kspApi.annotation.Capability
import com.tsarsprocket.reportmid.matchDetails.api.di.MatchDetailsApi

@Capability(
    api = MatchDetailsApi::class,
    modules = [
        MatchDetailsMainModule::class,
    ],
    dependencies = [
        AppApi::class,
    ],
)
internal interface MatchDetailsCapability