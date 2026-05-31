package com.tsarsprocket.reportmid.matchUpView.impl.viewIntent

import com.tsarsprocket.reportmid.kspApi.annotation.Intent
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.AccountInfo
import kotlinx.parcelize.Parcelize

@Parcelize
@Intent
internal data class ParticipantAccountLoadedIntent(
    val accountInfo: AccountInfo,
    val teamId: Int,
    val puuid: String,
) : InternalIntent
