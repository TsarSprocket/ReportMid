package com.tsarsprocket.reportmid.matchUpView.impl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.currentGameData.api.di.CurrentGameDataApi
import com.tsarsprocket.reportmid.dataDragonApi.di.DataDragonApi
import com.tsarsprocket.reportmid.kspApi.annotation.Capability
import com.tsarsprocket.reportmid.matchUpView.api.di.MatchUpViewApi
import com.tsarsprocket.reportmid.summonerApi.di.SummonerApi

@PerApi
@Capability(
    api = MatchUpViewApi::class,
    dependencies = [
        AppApi::class,
        CurrentGameDataApi::class,
        DataDragonApi::class,
        SummonerApi::class,
    ],
    modules = [
        MainModule::class,
    ]
)
internal interface MatchUpViewCapability
