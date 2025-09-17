package com.tsarsprocket.reportmid.summonerViewImpl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Reducer
import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.summonerViewApi.viewIntent.SummonerViewIntent
import com.tsarsprocket.reportmid.summonerViewImpl.viewIntent.InternalSummonerViewIntent
import com.tsarsprocket.reportmid.summonerViewImpl.viewIntent.ShowMatchHistory
import com.tsarsprocket.reportmid.summonerViewImpl.viewIntent.ShowProfile
import com.tsarsprocket.reportmid.summonerViewImpl.viewState.SummonerViewState
import com.tsarsprocket.reportmid.summonerViewImpl.viewState.SummonerViewState.ActivePage.MATCH_HISTORY
import com.tsarsprocket.reportmid.summonerViewImpl.viewState.SummonerViewState.ActivePage.PROFILE
import com.tsarsprocket.reportmid.viewStateApi.reducer.ViewStateReducer
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import javax.inject.Inject

@PerApi
@Reducer(
    explicitIntents = [
        SummonerViewIntent::class,
    ],
)
class SummonerViewReducer @Inject constructor() : ViewStateReducer {

    override suspend fun reduce(intent: ViewIntent, state: ViewState, stateHolder: ViewStateHolder): ViewState {
        return when(intent) {
            is SummonerViewIntent -> stateHolder.initializeSummonerView(intent.puuid, intent.region)
            is InternalSummonerViewIntent -> when(intent) {
                ShowProfile -> (state as? SummonerViewState)?.copy(activePage = PROFILE) ?: state
                ShowMatchHistory -> (state as? SummonerViewState)?.copy(activePage = MATCH_HISTORY) ?: state
            }
            else -> super.reduce(intent, state, stateHolder)
        }
    }

    private fun ViewStateHolder.initializeSummonerView(puuid: String, region: Region): ViewState {
        return SummonerViewState(
            profileOverviewStateHolder = createSubholder(),
            matchHistoryStateHolder = createSubholder(),
            summonerPuuid = puuid,
            summonerRegion = region,
            activePage = PROFILE,
        )
    }
}