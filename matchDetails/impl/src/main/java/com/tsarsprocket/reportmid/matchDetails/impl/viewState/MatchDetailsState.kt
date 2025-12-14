package com.tsarsprocket.reportmid.matchDetails.impl.viewState

import com.tsarsprocket.reportmid.kspApi.annotation.State
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import kotlinx.collections.immutable.ImmutableList
import kotlinx.parcelize.Parcelize

@Parcelize
@State
internal data class MatchDetailsState(
    val region: Region,
    val gameType: String,
    val duration: String,
    val teams: ImmutableList<TeamInfo>,
) : AbstractMatchDetailsState
