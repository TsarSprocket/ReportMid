package com.tsarsprocket.reportmid.matchDetails.impl.visualizer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Visualizer
import com.tsarsprocket.reportmid.matchDetails.impl.viewState.AbstractMatchDetailsState
import com.tsarsprocket.reportmid.matchDetails.impl.viewState.LoadingState
import com.tsarsprocket.reportmid.matchDetails.impl.viewState.MatchDetailsState
import com.tsarsprocket.reportmid.matchDetails.impl.viewState.NotLoadedState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.visualizer.ViewStateVisualizer
import javax.inject.Inject

@PerApi
@Visualizer
internal class MatchDetailsVisualizer @Inject constructor() : ViewStateVisualizer {

    @Composable
    override fun Visualize(
        modifier: Modifier,
        state: ViewState,
        stateHolder: ViewStateHolder
    ) {
        if(state is AbstractMatchDetailsState) {
            with(stateHolder) {
                when(state) {
                    is MatchDetailsState -> ShowMatchDetails(modifier, state)
                    is LoadingState -> ShowLoading(modifier, state)
                    is NotLoadedState -> ShowNotLoaded(modifier, state)
                }
            }
        } else {
            super.Visualize(modifier, state, stateHolder)
        }
    }

    @Composable
    fun ViewStateHolder.ShowLoading(modifier: Modifier, state: LoadingState) {
        TODO("Not yet implemented")
    }

    @Composable
    fun ViewStateHolder.ShowMatchDetails(modifier: Modifier, state: MatchDetailsState) {
        TODO("Not yet implemented")
    }

    @Composable
    fun ViewStateHolder.ShowNotLoaded(modifier: Modifier, state: NotLoadedState) {
        TODO("Not yet implemented")
    }
}