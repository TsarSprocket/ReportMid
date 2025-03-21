package com.tsarsprocket.reportmid.landingImpl.visualizer

import androidx.compose.runtime.Composable
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Visualizer
import com.tsarsprocket.reportmid.landingImpl.view.DataDragonNotLoadedScreen
import com.tsarsprocket.reportmid.landingImpl.view.LandingScreen
import com.tsarsprocket.reportmid.landingImpl.viewIntent.InternalLandingIntent.TryReinitialize
import com.tsarsprocket.reportmid.landingImpl.viewState.InternalLandingViewState
import com.tsarsprocket.reportmid.landingImpl.viewState.InternalLandingViewState.DataDragonNotLoadedViewState
import com.tsarsprocket.reportmid.landingImpl.viewState.InternalLandingViewState.LandingPageViewState
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.visualizer.ViewStateVisualizer
import javax.inject.Inject

@PerApi
@Visualizer
class LandingVisualizer @Inject constructor() : ViewStateVisualizer {

    @Composable
    override fun Visualize(state: ViewState, stateHolder: ViewStateHolder) = ReportMidTheme {
        if(state is InternalLandingViewState) { // To make sure all substates are covered
            when(state) {
                is DataDragonNotLoadedViewState -> stateHolder.DataDragonNotLoaded(state.isLoading)
                is LandingPageViewState -> Landing()
            }
        } else super.Visualize(state, stateHolder)
    }

    @Composable
    private fun ViewStateHolder.DataDragonNotLoaded(isLoading: Boolean) {
        DataDragonNotLoadedScreen(isLoading) { postIntent(TryReinitialize) }
    }

    @Composable
    private fun Landing() {
        LandingScreen()
    }
}
