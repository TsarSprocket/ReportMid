package com.tsarsprocket.reportmid.viewStateApi.visualizer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tsarsprocket.reportmid.utils.common.logError
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder

interface ViewStateVisualizer {

    @Composable
    fun Visualize(modifier: Modifier, state: ViewState, stateHolder: ViewStateHolder) {
        logError("Unknown view state of class ${state::class.simpleName}")
    }
}
