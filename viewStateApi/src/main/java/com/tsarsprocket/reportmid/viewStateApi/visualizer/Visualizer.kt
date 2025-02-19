package com.tsarsprocket.reportmid.viewStateApi.visualizer

import androidx.compose.runtime.Composable
import com.tsarsprocket.reportmid.utils.common.logError
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder

fun interface Visualizer {
    @Composable
    fun Visualize(state: ViewState, stateHolder: ViewStateHolder)
}

fun Visualizer.handleUnknownState(state: ViewState) {
    logError("Unknown view state of class ${state::class.simpleName}")
}
