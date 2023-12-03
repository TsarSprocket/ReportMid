package com.tsarsprocket.reportmid.landing_impl.viewstate

import com.tsarsprocket.reportmid.base.di.PerApi
import com.tsarsprocket.reportmid.landing_impl.reducer.LandingStateReducer
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewIntent
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewState
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewStateHolder
import javax.inject.Inject

sealed class LandingViewIntent(protected val reducer: LandingStateReducer) : ViewIntent {

    @PerApi
    class LandingStartLoadIntent @Inject constructor(reducer: LandingStateReducer) : LandingViewIntent(reducer) {

        override suspend fun reduce(state: ViewState, stateHolder: ViewStateHolder): ViewState {
            return reducer.reduce(this)
        }
    }
}