package com.tsarsprocket.reportmid.view_state_api.view_state

import androidx.compose.runtime.Composable

interface StateVisualizer<State : ViewState> {
    @Composable
    fun Visualize(state: State, stateHolder: ViewStateHolder)
}