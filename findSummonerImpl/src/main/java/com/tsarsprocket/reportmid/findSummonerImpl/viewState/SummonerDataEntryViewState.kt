package com.tsarsprocket.reportmid.findSummonerImpl.viewState

import androidx.compose.runtime.Composable
import com.tsarsprocket.reportmid.findSummonerApi.viewIntent.FindSummonerResult
import com.tsarsprocket.reportmid.findSummonerImpl.view.SummonerDataEntryScreen
import com.tsarsprocket.reportmid.findSummonerImpl.viewIntent.FindAndConfirmSummonerViewIntent
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ReturnIntentFactory
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder

internal class SummonerDataEntryViewState(
    val returnIntentFactory: ReturnIntentFactory<FindSummonerResult>,
    val removeRecentState: Boolean,
) : ViewState {

    @Composable
    override fun Visualize(stateHolder: ViewStateHolder) {
        ReportMidTheme {
            SummonerDataEntryScreen { gameName, tagLine, region ->
                stateHolder.postIntent(
                    FindAndConfirmSummonerViewIntent(
                        gameName = gameName.value,
                        tagline = tagLine.value,
                        region = region
                    )
                )
            }
        }
    }
}