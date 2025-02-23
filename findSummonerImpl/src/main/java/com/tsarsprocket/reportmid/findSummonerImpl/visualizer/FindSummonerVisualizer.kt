package com.tsarsprocket.reportmid.findSummonerImpl.visualizer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.findSummonerApi.navigation.FindSummonerNavigation
import com.tsarsprocket.reportmid.findSummonerImpl.view.ConfirmSummonerScreen
import com.tsarsprocket.reportmid.findSummonerImpl.view.SummonerDataEntryScreen
import com.tsarsprocket.reportmid.findSummonerImpl.viewIntent.FindAndConfirmSummonerViewIntent
import com.tsarsprocket.reportmid.findSummonerImpl.viewState.ConfirmSummonerViewState
import com.tsarsprocket.reportmid.findSummonerImpl.viewState.SummonerDataEntryViewState
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.viewStateApi.navigation.Navigation
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.visualizer.Visualizer
import javax.inject.Inject

@PerApi
class FindSummonerVisualizer @Inject constructor(
    @Navigation(FindSummonerNavigation.TAG)
    private val navigation: FindSummonerNavigation,
) : Visualizer {

    @Composable
    override fun Visualize(state: ViewState, stateHolder: ViewStateHolder) = ReportMidTheme {
        when(state) {
            is ConfirmSummonerViewState -> stateHolder.ConfirmSummoner(state)
            is SummonerDataEntryViewState -> stateHolder.SummonerDataEntry()
            else -> super.Visualize(state, stateHolder)
        }
    }

    @Composable
    private fun ViewStateHolder.SummonerDataEntry() {
        SummonerDataEntryScreen { gameName, tagLine, region ->
            postIntent(
                FindAndConfirmSummonerViewIntent(
                    gameName = gameName.value,
                    tagline = tagLine.value,
                    region = region
                )
            )
        }
    }

    @Composable
    private fun ViewStateHolder.ConfirmSummoner(state: ConfirmSummonerViewState) {
        with(state.summonerData) {
            ConfirmSummonerScreen(
                summonerData = remember { this@with },
                confirmAction = { with(navigation) { returnSuccess(puuid.value, region) } },
                rejectAction = { with(navigation) { returnCancel() } },
            )
        }
    }
}