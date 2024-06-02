package com.tsarsprocket.reportmid.viewStateApi.view_state

interface ViewIntent {
    suspend fun reduce(state: ViewState, stateHolder: ViewStateHolder): ViewState
}