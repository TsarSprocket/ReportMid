package com.tsarsprocket.reportmid.viewStateApi.viewIntent

import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder

interface ViewStateReducer {
    suspend fun reduce(intent: ViewIntent, state: ViewState, stateHolder: ViewStateHolder): ViewState
}