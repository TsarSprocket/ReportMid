package com.tsarsprocket.reportmid.landingImpl.viewstate

import androidx.compose.runtime.Composable
import com.tsarsprocket.reportmid.landingImpl.view.Landing
import com.tsarsprocket.reportmid.viewStateApi.view_state.ViewState
import com.tsarsprocket.reportmid.viewStateApi.view_state.ViewStateHolder

internal class LandingViewState : ViewState {

    @Composable
    override fun Visualize(stateHolder: ViewStateHolder) {
        Landing()
    }
}