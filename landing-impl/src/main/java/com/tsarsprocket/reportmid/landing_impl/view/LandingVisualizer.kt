package com.tsarsprocket.reportmid.landing_impl.view

import androidx.compose.runtime.Composable
import com.tsarsprocket.reportmid.landing_impl.viewstate.LandingViewState
import com.tsarsprocket.reportmid.view_state_api.view_state.StateVisualizer
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewStateHolder
import javax.inject.Inject


internal class LandingVisualizer @Inject constructor() : StateVisualizer<LandingViewState> {

    @Composable
    override fun Visualize(state: LandingViewState, stateHolder: ViewStateHolder) {
        Landing()
    }
}
