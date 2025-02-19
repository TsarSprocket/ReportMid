package com.tsarsprocket.reportmid.viewStateImpl.visualizer

import androidx.compose.runtime.Composable
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.visualizer.Visualizer

class DefaultVisualizer : Visualizer {

    @Composable
    override fun Visualize(state: ViewState, stateHolder: ViewStateHolder) {
        // EmptyScreen does not require special processing
    }
}