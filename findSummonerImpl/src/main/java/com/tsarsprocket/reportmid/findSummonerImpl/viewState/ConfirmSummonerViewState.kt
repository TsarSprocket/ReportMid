package com.tsarsprocket.reportmid.findSummonerImpl.viewState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.tsarsprocket.reportmid.findSummonerApi.viewIntent.FindSummonerResult
import com.tsarsprocket.reportmid.findSummonerImpl.di.component
import com.tsarsprocket.reportmid.findSummonerImpl.domain.SummonerData
import com.tsarsprocket.reportmid.findSummonerImpl.view.ConfirmSummonerScreen
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ReturnIntentFactory
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder

internal class ConfirmSummonerViewState(
    private val summonerData: SummonerData,
    private val returnIntentFactory: ReturnIntentFactory<FindSummonerResult>,
    private val removeRecentState: Boolean,
) : ViewState {

    @Composable
    override fun Visualize(stateHolder: ViewStateHolder) {
        ReportMidTheme {
            ConfirmSummonerScreen(
                summonerData = remember { summonerData },
                confirmAction = {
                    if(removeRecentState) stateHolder.pop()
                    stateHolder.postIntent(returnIntentFactory.create(FindSummonerResult(summonerData.puuid, summonerData.region)))
                },
                rejectAction = {
                    stateHolder.postIntent(component.getFindSummonerViewIntentFactory().create(returnIntentFactory, removeRecentState))
                },
            )
        }
    }
}