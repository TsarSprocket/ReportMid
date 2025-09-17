package com.tsarsprocket.reportmid.findSummonerImpl.viewState

import com.tsarsprocket.reportmid.kspApi.annotation.State
import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import kotlinx.parcelize.Parcelize

@Parcelize
@State
internal class ConfirmSummonerViewState(
    val puuid: String,
    val region: Region,
    val playerName: String,
    val iconUrl: String,
    val level: Int,
) : ViewState
