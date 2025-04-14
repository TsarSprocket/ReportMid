package com.tsarsprocket.reportmid.mainScreenImpl.visualizer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Visualizer
import com.tsarsprocket.reportmid.mainScreenImpl.view.MainScreenView
import com.tsarsprocket.reportmid.mainScreenImpl.viewState.MainScreenViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.visualizer.ViewStateVisualizer
import javax.inject.Inject

@PerApi
@Visualizer
class MainScreenVisualizer @Inject constructor() : ViewStateVisualizer {

    @Composable
    override fun Visualize(modifier: Modifier, state: ViewState, stateHolder: ViewStateHolder) = if(state is MainScreenViewState) {
        MainScreen(modifier, state)
    } else super.Visualize(modifier, state, stateHolder)

    @Composable
    private fun MainScreen(modifier: Modifier, state: MainScreenViewState) {
        MainScreenView(modifier) { innerModifier ->
            state.summonerStateHolder.Visualize(innerModifier)
        }
    }
}