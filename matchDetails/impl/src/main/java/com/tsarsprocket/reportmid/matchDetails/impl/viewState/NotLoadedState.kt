package com.tsarsprocket.reportmid.matchDetails.impl.viewState

import com.tsarsprocket.reportmid.kspApi.annotation.State
import com.tsarsprocket.reportmid.lol.api.model.Region
import kotlinx.parcelize.Parcelize

@Parcelize
@State
internal data class NotLoadedState(
    val matchId: String,
    val region: Region,
) : AbstractMatchDetailsState
