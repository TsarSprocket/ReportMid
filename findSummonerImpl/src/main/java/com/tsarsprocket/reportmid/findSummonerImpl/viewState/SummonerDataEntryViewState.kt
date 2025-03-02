package com.tsarsprocket.reportmid.findSummonerImpl.viewState

import com.tsarsprocket.reportmid.kspApi.annotation.State
import com.tsarsprocket.reportmid.lol.model.GameName
import com.tsarsprocket.reportmid.lol.model.Region.Companion.NONEXISTENT_REGION_ID
import com.tsarsprocket.reportmid.lol.model.TagLine
import com.tsarsprocket.reportmid.utils.common.EMPTY_STRING
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
@State
internal data class SummonerDataEntryViewState(
    val gameName: @RawValue GameName = GameName(EMPTY_STRING),
    val tagLine: @RawValue TagLine = TagLine(EMPTY_STRING),
    val selectedRegionId: Long = NONEXISTENT_REGION_ID
) : ViewState
