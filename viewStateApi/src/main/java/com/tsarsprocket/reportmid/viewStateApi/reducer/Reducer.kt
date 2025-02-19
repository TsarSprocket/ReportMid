package com.tsarsprocket.reportmid.viewStateApi.reducer

import com.tsarsprocket.reportmid.utils.common.logError
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder

fun interface Reducer {
    suspend fun reduce(intent: ViewIntent, state: ViewState, stateHolder: ViewStateHolder): ViewState
}

fun Reducer.handleUnknownIntent(intent: ViewIntent, state: ViewState): ViewState {
    logError("Unknown view intent of class ${intent::class.simpleName}")
    return state
}