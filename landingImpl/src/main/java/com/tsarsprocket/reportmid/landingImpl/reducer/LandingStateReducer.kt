package com.tsarsprocket.reportmid.landingImpl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.landingImpl.viewstate.LandingViewIntent
import com.tsarsprocket.reportmid.landingImpl.viewstate.LandingViewIntent.LandingStartLoadIntent
import com.tsarsprocket.reportmid.viewStateApi.view_state.ViewState
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