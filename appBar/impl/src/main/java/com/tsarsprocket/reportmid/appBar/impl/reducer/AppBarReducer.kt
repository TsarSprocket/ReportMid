package com.tsarsprocket.reportmid.appBar.impl.reducer

import com.tsarsprocket.reportmid.appBar.api.viewIntent.AppBarIntent
import com.tsarsprocket.reportmid.appBar.impl.viewState.InternalAppBarViewState
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Reducer
import com.tsarsprocket.reportmid.viewStateApi.reducer.ViewStateReducer
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import javax.inject.Inject

@PerApi
@Reducer(explicitIntents = [AppBarIntent::class])
internal class AppBarReducer @Inject constructor() : ViewStateReducer {

    override suspend fun reduce(intent: ViewIntent, state: ViewState, stateHolder: ViewStateHolder): ViewState {
        return when (intent) {
            is AppBarIntent -> state as InternalAppBarViewState
            else -> super.reduce(intent, state, stateHolder)
        }
    }
}
