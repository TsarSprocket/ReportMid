package com.tsarsprocket.reportmid.matchDetails.impl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Reducer
import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.matchDetails.api.viewIntent.MatchDetailsIntent
import com.tsarsprocket.reportmid.matchDetails.impl.domain.interactor.MatchDetailsInteractor
import com.tsarsprocket.reportmid.matchDetails.impl.viewIntent.AbstractInternalMatchDetailsIntent
import com.tsarsprocket.reportmid.matchDetails.impl.viewIntent.LoadMatchDataIntent
import com.tsarsprocket.reportmid.matchDetails.impl.viewState.LoadingState
import com.tsarsprocket.reportmid.matchDetails.impl.viewState.NotLoadedState
import com.tsarsprocket.reportmid.utils.common.logError
import com.tsarsprocket.reportmid.viewStateApi.reducer.ViewStateReducer
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import javax.inject.Inject

@PerApi
@Reducer(
    explicitIntents = [
        MatchDetailsIntent::class,
    ]
)
internal class MatchDetailsReducer @Inject constructor(
    private val interactor: MatchDetailsInteractor,
    private val mapper: MatchDetailsDataToStateMapper,
) : ViewStateReducer {

    override suspend fun reduce(intent: ViewIntent, state: ViewState, stateHolder: ViewStateHolder): ViewState {
        return with(stateHolder) {
            when(intent) {
                is MatchDetailsIntent -> startLoadMatchDetails(intent.matchId, intent.region)
                is AbstractInternalMatchDetailsIntent -> when(intent) {
                    is LoadMatchDataIntent -> loadMatchDetails(intent.matchId, intent.region)
                }

                else -> super.reduce(intent, state, stateHolder)
            }
        }
    }

    private fun ViewStateHolder.startLoadMatchDetails(matchId: String, region: Region): ViewState = LoadingState.also {
        postIntent(LoadMatchDataIntent(matchId, region))
    }

    private suspend fun ViewStateHolder.loadMatchDetails(matchId: String, region: Region): ViewState = try {
        mapper.map(interactor.getMatchDetails(matchId, region))
    } catch(exception: Exception) {
        logError("Error getting match data for match_id = $matchId, region = ${region.name}", exception)
        NotLoadedState(matchId, region)
    }
}
