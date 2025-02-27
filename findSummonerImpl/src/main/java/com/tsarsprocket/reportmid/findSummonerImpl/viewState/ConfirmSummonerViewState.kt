package com.tsarsprocket.reportmid.findSummonerImpl.viewState

import com.tsarsprocket.reportmid.findSummonerImpl.domain.SummonerData
import com.tsarsprocket.reportmid.kspApi.annotation.State
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import kotlinx.parcelize.Parcelize

@Parcelize
@State
internal class ConfirmSummonerViewState(
    val summonerData: SummonerData,
) : ViewState
