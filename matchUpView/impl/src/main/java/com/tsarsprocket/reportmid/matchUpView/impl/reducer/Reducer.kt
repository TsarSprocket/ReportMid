package com.tsarsprocket.reportmid.matchUpView.impl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Reducer
import com.tsarsprocket.reportmid.matchUpView.api.viewIntent.MatchUpIntent
import com.tsarsprocket.reportmid.matchUpView.impl.domain.Interactor
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.CurrentMatchUp
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.NoMatchUpFound
import com.tsarsprocket.reportmid.matchUpView.impl.viewIntent.LoadMatchUpIntent
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.ErrorState
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.LoadingState
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.NotFoundState
import com.tsarsprocket.reportmid.viewStateApi.reducer.ViewStateReducer
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import javax.inject.Inject

@PerApi
@Reducer(
    explicitIntents = [
        MatchUpIntent::class,
    ]
)
internal class Reducer @Inject constructor(
    private val interactor: Interactor,
    private val mapper: Mapper,
) : ViewStateReducer {

    override suspend fun reduce(intent: ViewIntent, state: ViewState, stateHolder: ViewStateHolder): ViewState {
        return when(intent) {
            is MatchUpIntent -> LoadingState(intent.puuid, intent.region)
            is LoadMatchUpIntent -> loadMatchUp(intent)
            else -> super.reduce(intent, state, stateHolder)
        }
    }

    private suspend fun loadMatchUp(intent: LoadMatchUpIntent): ViewState {
        return try {
            when(val result = interactor.getCurrentMatchUp(intent.puuid, intent.region)) {
                is CurrentMatchUp -> mapper.map(result, intent.puuid, intent.region)
                NoMatchUpFound -> NotFoundState(intent.puuid, intent.region)
            }
        } catch(exception: Exception) {
            ErrorState(intent.puuid, intent.region, exception.message ?: "Unknown error")
        }
    }
}
