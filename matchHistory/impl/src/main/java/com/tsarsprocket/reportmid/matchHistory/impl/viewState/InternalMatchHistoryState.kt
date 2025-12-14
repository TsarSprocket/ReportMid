package com.tsarsprocket.reportmid.matchHistory.impl.viewState

import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState

internal sealed interface InternalMatchHistoryState : ViewState {
    val puuid: String
    val region: Region
}
