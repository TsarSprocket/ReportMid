package com.tsarsprocket.reportmid.findSummonerImpl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.findSummonerImpl.domain.SummonerData
import com.tsarsprocket.reportmid.findSummonerImpl.viewState.ConfirmSummonerViewState
import javax.inject.Inject

@PerApi
internal class SummonerDataMapper @Inject constructor() {

    fun map(data: SummonerData): ConfirmSummonerViewState = with(data) {
        ConfirmSummonerViewState(
            puuid = puuid,
            region = region,
            playerName = "$gameName#$tagLine",
            iconUrl = iconUrl,
            level = level,
        )
    }
}