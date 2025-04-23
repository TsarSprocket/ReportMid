package com.tsarsprocket.reportmid.viewStateImpl.visualizer

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Visualizer
import com.tsarsprocket.reportmid.viewStateApi.viewState.EmptyScreenViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.visualizer.ViewStateVisualizer
import com.tsarsprocket.reportmid.viewStateImpl.viewState.LazyViewState
import javax.inject.Inject

@PerApi
@Visualizer(
    explicitStates = [
        EmptyScreenViewState::class,
    ],
)
class DefaultVisualizer @Inject constructor() : ViewStateVisualizer {

    @Composable
    override fun Visualize(modifier: Modifier, state: ViewState, stateHolder: ViewStateHolder) = when(state) {
        is EmptyScreenViewState -> EmptyScreen(modifier)
        is LazyViewState -> LazyScreen(modifier, state, stateHolder)
        else -> super.Visualize(modifier, state, stateHolder)
    }

    @Composable
    private fun LazyScreen(modifier: Modifier, state: LazyViewState, stateHolder: ViewStateHolder) {
        SideEffect {
            stateHolder.postIntent(state.intent)
        }
    }

    @Composable
    private fun EmptyScreen(modifier: Modifier) {
        Box(modifier)
    }
}