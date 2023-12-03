package com.tsarsprocket.reportmid.view_state_api.view_state

import androidx.compose.runtime.Composable

interface ViewState {
    @Composable
    fun Visualize(stateHolder: ViewStateHolder)
}
