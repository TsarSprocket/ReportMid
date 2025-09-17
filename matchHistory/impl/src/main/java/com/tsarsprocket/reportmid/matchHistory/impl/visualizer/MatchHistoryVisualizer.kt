package com.tsarsprocket.reportmid.matchHistory.impl.visualizer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Visualizer
import com.tsarsprocket.reportmid.matchHistory.impl.view.Loading
import com.tsarsprocket.reportmid.matchHistory.impl.view.MatchHistory
import com.tsarsprocket.reportmid.matchHistory.impl.viewIntent.LoadMoreIntent
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.InternalMatchHistoryState
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.LoadingMatchHistoryState
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.ShowingMatchHistoryState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.visualizer.ViewStateVisualizer
import javax.inject.Inject

@PerApi
@Visualizer
internal class MatchHistoryVisualizer @Inject constructor() : ViewStateVisualizer {

    @Composable
    override fun Visualize(modifier: Modifier, state: ViewState, stateHolder: ViewStateHolder) {
        if(state is InternalMatchHistoryState) {
            when(state) {
                is LoadingMatchHistoryState -> LoadingMatchHistory(modifier)
                is ShowingMatchHistoryState -> stateHolder.ShowingMatchHistory(modifier, state)
            }
        } else {
            super.Visualize(modifier, state, stateHolder)
        }
    }

    @Composable
    private fun LoadingMatchHistory(modifier: Modifier) {
        Loading(modifier)
    }

    @Composable
    private fun ViewStateHolder.ShowingMatchHistory(modifier: Modifier, state: ShowingMatchHistoryState) {
        MatchHistory(
            modifier = modifier,
            state = state,
        ) {
            postIntent(LoadMoreIntent)
        }
    }
}