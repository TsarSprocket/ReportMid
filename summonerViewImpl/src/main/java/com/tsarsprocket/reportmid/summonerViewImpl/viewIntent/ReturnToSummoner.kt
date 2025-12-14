package com.tsarsprocket.reportmid.summonerViewImpl.viewIntent

import com.tsarsprocket.reportmid.kspApi.annotation.Intent
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.summonerViewImpl.viewState.ActivePage
import kotlinx.parcelize.Parcelize

@Parcelize
@Intent
internal data class ReturnToSummoner(
    val puuid: String,
    val region: Region,
    val activePage: ActivePage,
) : InternalSummonerViewIntent
