package com.tsarsprocket.reportmid.view_state_impl.view_state

import androidx.compose.runtime.Composable
import com.tsarsprocket.reportmid.view_state_api.view_state.GeneralViewStateCluster
import com.tsarsprocket.reportmid.view_state_api.view_state.StateVisualizer
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewStateController
import javax.inject.Inject

internal class GeneralVisualizer @Inject constructor() : StateVisualizer<GeneralViewStateCluster> {

    @Composable
    override fun visualize(state: GeneralViewStateCluster, controller: ViewStateController) {
        // Show nothing
    }
}