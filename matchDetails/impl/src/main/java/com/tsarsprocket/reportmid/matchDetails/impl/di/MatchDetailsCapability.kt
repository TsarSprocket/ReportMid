package com.tsarsprocket.reportmid.matchDetails.impl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Capability
import com.tsarsprocket.reportmid.matchData.api.di.MatchDataApi
import com.tsarsprocket.reportmid.matchDetails.api.di.MatchDetailsApi

@PerApi
@Capability(
    api = MatchDetailsApi::class,
    modules = [
        MatchDetailsMainModule::class,
    ],
    dependencies = [
        AppApi::class,
        MatchDataApi::class,
    ],
)
internal interface MatchDetailsCapability