package com.tsarsprocket.reportmid.summonerViewImpl.visualizer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Visualizer
import com.tsarsprocket.reportmid.summonerViewImpl.view.SummonerView
import com.tsarsprocket.reportmid.summonerViewImpl.viewIntent.ShowMatchHistory
import com.tsarsprocket.reportmid.summonerViewImpl.viewIntent.ShowProfile
import com.tsarsprocket.reportmid.summonerViewImpl.viewState.SummonerViewState
import com.tsarsprocket.reportmid.summonerViewImpl.viewState.SummonerViewState.ActivePage.MATCH_HISTORY
import com.tsarsprocket.reportmid.summonerViewImpl.viewState.SummonerViewState.ActivePage.PROFILE
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.visualizer.ViewStateVisualizer
import javax.inject.Inject

@PerApi
@Visualizer
internal class SummonerViewVisualizer @Inject constructor() : ViewStateVisualizer {

    @Composable
    override fun Visualize(modifier: Modifier, state: ViewState, stateHolder: ViewStateHolder) = if(state is SummonerViewState) {
        stateHolder.ShowSummonerView(modifier, state)
    } else super.Visualize(modifier, state, stateHolder)

    @Composable
    private fun ViewStateHolder.ShowSummonerView(modifier: Modifier, state: SummonerViewState) {
        SummonerView(
            modifier = modifier,
            selected = state.activePage,
            selectProfile = { postIntent(ShowProfile) },
            selectMatchHistory = { postIntent(ShowMatchHistory) },
        ) { innerModifier ->
            when(state.activePage) {
                PROFILE -> state.profileOverviewStateHolder.Visualize(innerModifier)
                MATCH_HISTORY -> state.matchHistoryStateHolder.Visualize(innerModifier)
            }
        }
    }
}