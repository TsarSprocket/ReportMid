package com.tsarsprocket.reportmid.summonerViewImpl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Reducer
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.summonerViewApi.viewIntent.SummonerViewIntent
import com.tsarsprocket.reportmid.summonerViewImpl.viewIntent.InternalSummonerViewIntent
import com.tsarsprocket.reportmid.summonerViewImpl.viewIntent.ReturnToSummoner
import com.tsarsprocket.reportmid.summonerViewImpl.viewIntent.ShowMatchHistory
import com.tsarsprocket.reportmid.summonerViewImpl.viewIntent.ShowMatchUp
import com.tsarsprocket.reportmid.summonerViewImpl.viewIntent.ShowProfile
import com.tsarsprocket.reportmid.summonerViewImpl.viewState.ActivePage
import com.tsarsprocket.reportmid.summonerViewImpl.viewState.ActivePage.MATCH_HISTORY
import com.tsarsprocket.reportmid.summonerViewImpl.viewState.ActivePage.MATCH_UP
import com.tsarsprocket.reportmid.summonerViewImpl.viewState.ActivePage.PROFILE
import com.tsarsprocket.reportmid.summonerViewImpl.viewState.InternalSummonerViewState
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
            is SummonerViewIntent -> stateHolder.initializeSummonerView(puuid = intent.puuid, region = intent.region, activePage = PROFILE)
            is InternalSummonerViewIntent -> when(intent) {
                is ReturnToSummoner -> stateHolder.initializeSummonerView(puuid = intent.puuid, region = intent.region, activePage = intent.activePage)
                ShowProfile -> state.apply { (this as? InternalSummonerViewState)?.activePage = PROFILE }
                ShowMatchUp -> state.apply { (this as? InternalSummonerViewState)?.activePage = MATCH_UP }
                ShowMatchHistory -> state.apply { (this as? InternalSummonerViewState)?.activePage = MATCH_HISTORY }
            }
            else -> super.reduce(intent, state, stateHolder)
        }
    }

    private fun ViewStateHolder.initializeSummonerView(puuid: String, region: Region, activePage: ActivePage): ViewState = InternalSummonerViewState(
        profileOverviewStateHolder = createSubholder(),
        matchUpStateHolder = createSubholder(),
        matchHistoryStateHolder = createSubholder(),
        summonerPuuid = puuid,
        summonerRegion = region,
        initialActivePage = activePage,
    )
}