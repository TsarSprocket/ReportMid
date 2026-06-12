package com.tsarsprocket.reportmid.matchUpView.impl.viewIntent

import com.tsarsprocket.reportmid.kspApi.annotation.Intent
import kotlinx.parcelize.Parcelize

@Parcelize
@Intent
internal data class SetSelectedTeamIndexIntent(
    val index: Int,
) : InternalIntent
