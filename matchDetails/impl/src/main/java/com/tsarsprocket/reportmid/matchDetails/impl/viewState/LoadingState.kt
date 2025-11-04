package com.tsarsprocket.reportmid.matchDetails.impl.viewState

import com.tsarsprocket.reportmid.kspApi.annotation.State
import kotlinx.parcelize.Parcelize

@Parcelize
@State
internal data object LoadingState : AbstractMatchDetailsState
