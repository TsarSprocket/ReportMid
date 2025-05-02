package com.tsarsprocket.reportmid.matchData.impl.di

import com.tsarsprocket.reportmid.kspApi.annotation.Capability
import com.tsarsprocket.reportmid.lolServicesApi.di.LolServicesApi
import com.tsarsprocket.reportmid.matchData.api.di.MatchDataApi

@Capability(
    api = MatchDataApi::class,
    dependencies = [
        LolServicesApi::class,
    ],
)
interface MatchDataCapability
