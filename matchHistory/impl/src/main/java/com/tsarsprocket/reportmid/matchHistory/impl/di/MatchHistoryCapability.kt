package com.tsarsprocket.reportmid.matchHistory.impl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Capability
import com.tsarsprocket.reportmid.matchHistory.api.di.MatchHistoryApi

@PerApi
@Capability(
    api = MatchHistoryApi::class,
)
interface MatchHistoryCapability
