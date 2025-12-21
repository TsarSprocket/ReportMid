package com.tsarsprocket.reportmid.matchDetails.impl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.dataDragonApi.di.DataDragonApi
import com.tsarsprocket.reportmid.kspApi.annotation.Capability
import com.tsarsprocket.reportmid.lol.api.di.LolApi
import com.tsarsprocket.reportmid.matchData.api.di.MatchDataApi
import com.tsarsprocket.reportmid.matchDetails.api.di.MatchDetailsApi
import com.tsarsprocket.reportmid.navigationMapApi.di.NavigationMapApi

@PerApi
@Capability(
    api = MatchDetailsApi::class,
    modules = [
        MatchDetailsMainModule::class,
    ],
    dependencies = [
        AppApi::class,
        DataDragonApi::class,
        LolApi::class,
        MatchDataApi::class,
        NavigationMapApi::class,
    ],
)
internal interface MatchDetailsCapability