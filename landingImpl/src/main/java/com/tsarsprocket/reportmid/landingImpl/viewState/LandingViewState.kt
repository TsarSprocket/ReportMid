package com.tsarsprocket.reportmid.landingImpl.viewState

import androidx.compose.runtime.Composable
import com.tsarsprocket.reportmid.landingImpl.view.LandingScreen
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder

internal class LandingViewState : ViewState {

    @Composable
    override fun Visualize(stateHolder: ViewStateHolder) {
        LandingScreen()
    }
}