package com.tsarsprocket.reportmid.view_state_api.view_state


interface StateReducer<Intent : ViewIntent> {
    suspend fun reduce(state: ViewState, intent: Intent, controller: ViewStateController): ViewState
}