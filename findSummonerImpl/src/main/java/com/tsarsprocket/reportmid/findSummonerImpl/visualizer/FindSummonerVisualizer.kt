package com.tsarsprocket.reportmid.findSummonerImpl.visualizer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.findSummonerApi.navigation.FindSummonerNavigation
import com.tsarsprocket.reportmid.findSummonerImpl.view.ConfirmSummonerScreen
import com.tsarsprocket.reportmid.findSummonerImpl.view.SummonerDataEntryScreen
import com.tsarsprocket.reportmid.findSummonerImpl.viewState.ConfirmSummonerViewState
import com.tsarsprocket.reportmid.findSummonerImpl.viewState.SummonerDataEntryViewState
import com.tsarsprocket.reportmid.kspApi.annotation.Visualizer
import com.tsarsprocket.reportmid.viewStateApi.navigation.Navigation
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.visualizer.ViewStateVisualizer
import javax.inject.Inject

@PerApi
@Visualizer
class FindSummonerVisualizer @Inject constructor(
    @Navigation(FindSummonerNavigation.TAG)
    private val navigation: FindSummonerNavigation,
) : ViewStateVisualizer {

    @Composable
    override fun Visualize(modifier: Modifier, state: ViewState, stateHolder: ViewStateHolder) = when(state) {
        is ConfirmSummonerViewState -> stateHolder.ConfirmSummoner(modifier, state)
        is SummonerDataEntryViewState -> stateHolder.SummonerDataEntry(modifier, state)
        else -> super.Visualize(modifier, state, stateHolder)
    }

    @Composable
    private fun ViewStateHolder.SummonerDataEntry(modifier: Modifier, state: SummonerDataEntryViewState) {
        SummonerDataEntryScreen(modifier, state)
    }

    @Composable
    private fun ViewStateHolder.ConfirmSummoner(modifier: Modifier, state: ConfirmSummonerViewState) {
        with(state.summonerData) {
            ConfirmSummonerScreen(
                modifier = modifier,
                summonerData = remember { this@with },
                confirmAction = { with(navigation) { returnSuccess(puuid.value, region) } },
                rejectAction = { with(navigation) { returnCancel() } },
            )
        }
    }
}