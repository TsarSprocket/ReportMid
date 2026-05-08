package com.tsarsprocket.reportmid.matchUpView.impl.visualizer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Visualizer
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.visualizer.ViewStateVisualizer
import javax.inject.Inject

@PerApi
@Visualizer
internal class MatchUpViewVisualizer @Inject constructor() : ViewStateVisualizer {

    @Composable
    override fun Visualize(modifier: Modifier, state: ViewState, stateHolder: ViewStateHolder) {
        // TODO: Implement visualization
    }
}
