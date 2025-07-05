package com.tsarsprocket.reportmid.mainScreenImpl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Reducer
import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.mainScreenApi.constants.SUMMONER_VIEW_TAG
import com.tsarsprocket.reportmid.mainScreenApi.viewIntent.MainScreenViewIntent
import com.tsarsprocket.reportmid.mainScreenImpl.viewState.MainScreenViewState
import com.tsarsprocket.reportmid.viewStateApi.reducer.ViewStateReducer
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import javax.inject.Inject

@PerApi
@Reducer(
    explicitIntents = [
        MainScreenViewIntent::class,
    ],
)
class MainScreenReducer @Inject constructor() : ViewStateReducer {

    override suspend fun reduce(intent: ViewIntent, state: ViewState, stateHolder: ViewStateHolder): ViewState {
        return when(intent) {
            is MainScreenViewIntent -> stateHolder.createMainScreen(intent.puuid, intent.region)
            else -> super.reduce(intent, state, stateHolder)
        }
    }

    private fun ViewStateHolder.createMainScreen(initialPuuid: String, initialRegion: Region): ViewState {
        return MainScreenViewState(
            summonerStateHolder = createSubholder(tag = SUMMONER_VIEW_TAG),
            initialSummonerPuuid = initialPuuid,
            initialSummonerRegion = initialRegion
        )
    }
}