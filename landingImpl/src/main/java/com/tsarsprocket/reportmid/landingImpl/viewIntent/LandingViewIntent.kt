package com.tsarsprocket.reportmid.landingImpl.viewIntent

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.landingImpl.reducer.LandingStateReducer
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder
import javax.inject.Inject

internal sealed class LandingViewIntent(private val reducer: LandingStateReducer) : ViewIntent {

    override suspend fun reduce(state: ViewState, stateHolder: ViewStateHolder): ViewState {
        return reducer.reduce(this, stateHolder)
    }

    class DataDragonNotLoadedViewIntent(reducer: LandingStateReducer) : LandingViewIntent(reducer)

    @PerApi
    class LandingStartLoadIntent @Inject constructor(reducer: LandingStateReducer) : LandingViewIntent(reducer)
}