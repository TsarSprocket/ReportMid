package com.tsarsprocket.reportmid.matchHistory.impl.viewState

import com.tsarsprocket.reportmid.kspApi.annotation.State
import com.tsarsprocket.reportmid.lol.api.model.Region
import kotlinx.parcelize.Parcelize

@Parcelize
@State
internal data class LoadingMatchHistoryState(
    override val puuid: String,
    override val region: Region,
) : InternalMatchHistoryState