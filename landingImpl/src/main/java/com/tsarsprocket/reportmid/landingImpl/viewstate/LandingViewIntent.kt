package com.tsarsprocket.reportmid.landingImpl.viewstate

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.landingImpl.reducer.LandingStateReducer
import com.tsarsprocket.reportmid.viewStateApi.view_state.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.view_state.ViewState
import com.tsarsprocket.reportmid.viewStateApi.view_state.ViewStateHolder
import javax.inject.Inject

sealed class LandingViewIntent(protected val reducer: LandingStateReducer) : ViewIntent {

    @PerApi
    class LandingStartLoadIntent @Inject constructor(reducer: LandingStateReducer) : LandingViewIntent(reducer) {

        override suspend fun reduce(state: ViewState, stateHolder: ViewStateHolder): ViewState {
            return reducer.reduce(this)
        }
    }
}