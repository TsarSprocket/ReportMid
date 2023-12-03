package com.tsarsprocket.reportmid.view_state_api.view_state

interface ViewIntent {
    suspend fun reduce(state: ViewState, stateHolder: ViewStateHolder): ViewState
}