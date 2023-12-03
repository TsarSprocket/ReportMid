package com.tsarsprocket.reportmid.landing_impl.reducer

import com.tsarsprocket.reportmid.base.di.PerApi
import com.tsarsprocket.reportmid.landing_impl.viewstate.LandingViewIntent
import com.tsarsprocket.reportmid.landing_impl.viewstate.LandingViewIntent.LandingStartLoadIntent
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewState
import javax.inject.Inject

@PerApi
class LandingStateReducer @Inject constructor() {

    fun reduce(intent: LandingViewIntent): ViewState {
        return when (intent) {
            is LandingStartLoadIntent -> startLoading()
        }
    }

    private fun startLoading(): ViewState {
        TODO("Not yet implemented")
    }
}