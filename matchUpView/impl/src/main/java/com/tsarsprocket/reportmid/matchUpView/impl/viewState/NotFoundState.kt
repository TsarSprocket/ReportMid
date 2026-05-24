package com.tsarsprocket.reportmid.matchUpView.impl.viewState

import com.tsarsprocket.reportmid.kspApi.annotation.State
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import kotlinx.parcelize.Parcelize

@Parcelize
@State
internal data class NotFoundState(
    val puuid: String,
    val region: Region,
) : InternalViewState
