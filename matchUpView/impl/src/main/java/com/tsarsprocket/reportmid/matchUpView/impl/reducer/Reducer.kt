package com.tsarsprocket.reportmid.matchUpView.impl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Reducer
import com.tsarsprocket.reportmid.matchUpView.api.viewIntent.MatchUpViewIntent
import com.tsarsprocket.reportmid.matchUpView.impl.domain.Interactor
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.CurrentMatchUp
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.NoMatchUpFound
import com.tsarsprocket.reportmid.matchUpView.impl.viewIntent.LoadMatchUpViewIntent
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.ErrorViewState
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.LoadingViewState
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.NotFoundViewState
import com.tsarsprocket.reportmid.viewStateApi.reducer.ViewStateReducer
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import javax.inject.Inject

@PerApi
@Reducer(
    explicitIntents = [
        MatchUpViewIntent::class,
    ]
)
internal class Reducer @Inject constructor(
    private val interactor: Interactor,
    private val mapper: Mapper,
) : ViewStateReducer {

    override suspend fun reduce(intent: ViewIntent, state: ViewState, stateHolder: ViewStateHolder): ViewState {
        return when(intent) {
            is MatchUpViewIntent -> LoadingViewState(intent.puuid, intent.region)
            is LoadMatchUpViewIntent -> loadMatchUp(intent)
            else -> super.reduce(intent, state, stateHolder)
        }
    }

    private suspend fun loadMatchUp(intent: LoadMatchUpViewIntent): ViewState {
        return try {
            when(val result = interactor.getCurrentMatchUp(intent.puuid, intent.region)) {
                is CurrentMatchUp -> mapper.map(result)
                NoMatchUpFound -> NotFoundViewState(intent.puuid, intent.region)
            }
        } catch(exception: Exception) {
            ErrorViewState(intent.puuid, intent.region, exception.message ?: "Unknown error")
        }
    }
}
