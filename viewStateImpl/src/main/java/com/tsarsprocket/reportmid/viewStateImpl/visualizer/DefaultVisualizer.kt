package com.tsarsprocket.reportmid.viewStateImpl.visualizer

import androidx.compose.runtime.Composable
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Visualizer
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.viewStateApi.viewState.EmptyScreenViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.visualizer.ViewStateVisualizer
import javax.inject.Inject

@PerApi
@Visualizer(
    explicitStates = [
        EmptyScreenViewState::class,
    ],
)
class DefaultVisualizer @Inject constructor() : ViewStateVisualizer {

    @Composable
    override fun Visualize(state: ViewState, stateHolder: ViewStateHolder) = ReportMidTheme {
        if(state is EmptyScreenViewState) {
            // EmptyScreen does not require special processing
        } else super.Visualize(state, stateHolder)
    }
}