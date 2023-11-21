package com.tsarsprocket.reportmid.landing_impl.reducer

import com.tsarsprocket.reportmid.landing_impl.viewstate.LandingViewIntent
import com.tsarsprocket.reportmid.landing_impl.viewstate.LandingViewIntent.LandingStartLoadIntent
import com.tsarsprocket.reportmid.view_state_api.view_state.StateReducer
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewState
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewStateHolder
import javax.inject.Inject

class LandingStateReducer @Inject constructor() : StateReducer<LandingViewIntent> {

    override suspend fun reduce(state: ViewState, intent: LandingViewIntent, controller: ViewStateHolder): ViewState {
        return when (intent) {
            is LandingStartLoadIntent -> startLoading()
        }
    }

    private fun startLoading(): ViewState {
        TODO("Not yet implemented")
    }
}