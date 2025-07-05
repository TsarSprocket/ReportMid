package com.tsarsprocket.reportmid.findSummonerImpl.viewState

import com.tsarsprocket.reportmid.kspApi.annotation.State
import com.tsarsprocket.reportmid.lol.api.model.Region.Companion.ID_NONEXISTENT_REGION
import com.tsarsprocket.reportmid.utils.common.EMPTY_STRING
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import kotlinx.parcelize.Parcelize

@Parcelize
@State
data class SummonerDataEntryViewState(
    val gameName: String = EMPTY_STRING,
    val tagLine: String = EMPTY_STRING,
    val selectedRegionId: Int = ID_NONEXISTENT_REGION
) : ViewState
