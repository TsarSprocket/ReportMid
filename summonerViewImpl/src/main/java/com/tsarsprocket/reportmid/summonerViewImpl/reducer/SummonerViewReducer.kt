package com.tsarsprocket.reportmid.summonerViewImpl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Reducer
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.summonerViewApi.viewIntent.SummonerViewIntent
import com.tsarsprocket.reportmid.summonerViewImpl.viewState.SummonerViewState
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
            else -> super.reduce(intent, state, stateHolder)
        }
    }

    private fun ViewStateHolder.initializeSummonerView(puuid: String, region: Region): ViewState {
        return SummonerViewState(createSubholder(), puuid, region)
    }
}