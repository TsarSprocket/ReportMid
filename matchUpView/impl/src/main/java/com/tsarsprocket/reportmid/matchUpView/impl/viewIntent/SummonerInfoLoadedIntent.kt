package com.tsarsprocket.reportmid.matchUpView.impl.viewIntent

import com.tsarsprocket.reportmid.kspApi.annotation.Intent
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.SummonerInfo
import kotlinx.parcelize.Parcelize

@Parcelize
@Intent
internal data class SummonerInfoLoadedIntent(
    val summonerInfo: SummonerInfo,
    val teamId: Int,
    val puuid: String,
) : InternalIntent
