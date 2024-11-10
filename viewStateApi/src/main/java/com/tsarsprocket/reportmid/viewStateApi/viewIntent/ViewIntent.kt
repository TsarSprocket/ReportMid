package com.tsarsprocket.reportmid.viewStateApi.viewIntent

import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder

interface ViewIntent {
    suspend fun reduce(state: ViewState, stateHolder: ViewStateHolder): ViewState
}
