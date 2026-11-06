package com.tsarsprocket.reportmid.appBar.impl.visualizer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tsarsprocket.reportmid.appBar.impl.view.AppBarView
import com.tsarsprocket.reportmid.appBar.impl.viewState.AppBarState
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Visualizer
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.visualizer.ViewStateVisualizer
import javax.inject.Inject

@PerApi
@Visualizer
internal class AppBarVisualizer @Inject constructor() : ViewStateVisualizer {

    @Composable
    override fun Visualize(modifier: Modifier, state: ViewState, stateHolder: ViewStateHolder) {
        if(state is AppBarState) {
            AppBarView(modifier, state)
        } else {
            super.Visualize(modifier, state, stateHolder)
        }
    }
}
