package com.tsarsprocket.reportmid.matchUpView.impl.visualizer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Visualizer
import com.tsarsprocket.reportmid.matchUpView.impl.view.Loading
import com.tsarsprocket.reportmid.matchUpView.impl.view.MatchUp
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.InternalViewState
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.LoadingState
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.MatchUpState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.visualizer.ViewStateVisualizer
import javax.inject.Inject

@PerApi
@Visualizer
internal class Visualizer @Inject constructor() : ViewStateVisualizer {

    @Composable
    override fun Visualize(modifier: Modifier, state: ViewState, stateHolder: ViewStateHolder) {
        if(state is InternalViewState) {
            when(state) {
                is LoadingState -> Loading(modifier, state, stateHolder)
                is MatchUpState -> MatchUp(modifier, state, stateHolder)
                else -> super.Visualize(modifier, state, stateHolder)
            }
        } else {
            super.Visualize(modifier, state, stateHolder)
        }
    }
}
