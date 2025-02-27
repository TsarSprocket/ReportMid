package com.tsarsprocket.reportmid.viewStateApi.visualizer

import androidx.compose.runtime.Composable
import com.tsarsprocket.reportmid.utils.common.logError
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder

interface StateVisualizer {

    @Composable
    fun Visualize(state: ViewState, stateHolder: ViewStateHolder) {
        logError("Unknown view state of class ${state::class.simpleName}")
    }
}
