package com.tsarsprocket.reportmid.matchUpView.impl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.currentGameData.api.di.CurrentGameDataApi
import com.tsarsprocket.reportmid.kspApi.annotation.Capability
import com.tsarsprocket.reportmid.matchUpView.api.di.MatchUpViewApi

@PerApi
@Capability(
    api = MatchUpViewApi::class,
    dependencies = [
        AppApi::class,
        CurrentGameDataApi::class,
    ],
    modules = [
        MainModule::class,
    ]
)
internal interface Capability
