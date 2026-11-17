package com.tsarsprocket.reportmid.summonerViewImpl.viewIntent

import com.tsarsprocket.reportmid.appBar.api.viewIntent.Iconized
import com.tsarsprocket.reportmid.summonerViewImpl.viewState.InternalSummonerViewState
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class ShowSummonerState(
    val summonerSate: InternalSummonerViewState,
    override val iconUrl: String,
) : InternalSummonerViewIntent, Iconized
