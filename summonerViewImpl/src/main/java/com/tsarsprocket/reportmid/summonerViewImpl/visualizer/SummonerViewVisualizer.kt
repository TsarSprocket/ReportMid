package com.tsarsprocket.reportmid.summonerViewImpl.visualizer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Visualizer
import com.tsarsprocket.reportmid.summonerViewImpl.view.SummonerView
import com.tsarsprocket.reportmid.summonerViewImpl.viewState.SummonerViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.visualizer.ViewStateVisualizer
import javax.inject.Inject

@PerApi
@Visualizer
class SummonerViewVisualizer @Inject constructor() : ViewStateVisualizer {

    @Composable
    override fun Visualize(modifier: Modifier, state: ViewState, stateHolder: ViewStateHolder) = if(state is SummonerViewState) {
        SummonerView(modifier, state)
    } else super.Visualize(modifier, state, stateHolder)

    @Composable
    private fun SummonerView(modifier: Modifier, state: SummonerViewState) {
        SummonerView(modifier = modifier) { innerModifier ->
            state.profileOverviewStateHolder.Visualize(innerModifier)
        }
    }
}