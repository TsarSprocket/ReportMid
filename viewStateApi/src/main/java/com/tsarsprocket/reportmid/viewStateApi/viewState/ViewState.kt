package com.tsarsprocket.reportmid.viewStateApi.viewState

import androidx.compose.runtime.Composable

interface ViewState {
    @Composable
    fun Visualize(stateHolder: ViewStateHolder)
}
