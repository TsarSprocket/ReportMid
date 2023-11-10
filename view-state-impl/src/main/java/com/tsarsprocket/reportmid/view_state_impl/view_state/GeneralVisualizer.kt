package com.tsarsprocket.reportmid.view_state_impl.view_state

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.tsarsprocket.reportmid.view_state_api.view_state.GeneralViewStateCluster
import com.tsarsprocket.reportmid.view_state_api.view_state.StateVisualizer
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewStateHolder
import javax.inject.Inject

internal class GeneralVisualizer @Inject constructor() : StateVisualizer<GeneralViewStateCluster> {

    @Composable
    override fun Visualize(state: GeneralViewStateCluster, stateHolder: ViewStateHolder) {
        // EMPTY
    }
}