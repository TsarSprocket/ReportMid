package com.tsarsprocket.reportmid.matchHistory.impl.viewIntent

import com.tsarsprocket.reportmid.kspApi.annotation.Intent
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.MatchInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.parcelize.Parcelize

@Parcelize
@Intent
internal data class ShowHistoryDataIntent(
    val listOfMatchInfos: ImmutableList<MatchInfo>,
    val hasMoreToLoad: Boolean,
) : InternalMatchHistoryIntent