package com.tsarsprocket.reportmid.matchDetails.impl.viewIntent

import com.tsarsprocket.reportmid.kspApi.annotation.Intent
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import kotlinx.parcelize.Parcelize

@Parcelize
@Intent
data class LoadMatchDataIntent(
    val matchId: String,
    val region: Region,
) : AbstractInternalMatchDetailsIntent
