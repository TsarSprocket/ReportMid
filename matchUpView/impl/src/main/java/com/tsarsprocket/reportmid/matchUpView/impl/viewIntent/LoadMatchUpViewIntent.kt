package com.tsarsprocket.reportmid.matchUpView.impl.viewIntent

import com.tsarsprocket.reportmid.kspApi.annotation.Intent
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import kotlinx.parcelize.Parcelize

@Parcelize
@Intent
internal data class LoadMatchUpViewIntent(
    val puuid: String,
    val region: Region,
) : InternalViewIntent
