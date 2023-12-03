package com.tsarsprocket.reportmid.landing_impl.viewstate

import androidx.compose.runtime.Composable
import com.tsarsprocket.reportmid.landing_impl.view.Landing
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewState
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewStateHolder

internal class LandingViewState : ViewState {

    @Composable
    override fun Visualize(stateHolder: ViewStateHolder) {
        Landing()
    }
}