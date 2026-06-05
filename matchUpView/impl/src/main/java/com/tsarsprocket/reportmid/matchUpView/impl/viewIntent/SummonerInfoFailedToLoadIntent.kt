package com.tsarsprocket.reportmid.matchUpView.impl.viewIntent

import com.tsarsprocket.reportmid.kspApi.annotation.Intent
import kotlinx.parcelize.Parcelize

@Parcelize
@Intent
internal data class SummonerInfoFailedToLoadIntent(
    val teamId: Int,
    val puuid: String,
) : InternalIntent
