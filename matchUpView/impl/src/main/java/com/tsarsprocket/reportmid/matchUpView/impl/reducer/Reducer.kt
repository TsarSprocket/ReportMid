package com.tsarsprocket.reportmid.matchUpView.impl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Reducer
import com.tsarsprocket.reportmid.matchUpView.api.viewIntent.MatchUpViewIntent
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
internal class Reducer @Inject constructor() : ViewStateReducer {

    override suspend fun reduce(intent: ViewIntent, state: ViewState, stateHolder: ViewStateHolder): ViewState {
        TODO("Not yet implemented")
    }
}
