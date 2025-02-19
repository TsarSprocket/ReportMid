package com.tsarsprocket.reportmid.landingImpl.visualizer

import androidx.compose.runtime.Composable
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.landingImpl.view.LandingScreen
import com.tsarsprocket.reportmid.landingImpl.viewState.InternalLandingViewState
import com.tsarsprocket.reportmid.landingImpl.viewState.InternalLandingViewState.DataDragonNotLoadedViewState
import com.tsarsprocket.reportmid.landingImpl.viewState.InternalLandingViewState.LandingPageViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.visualizer.Visualizer
import com.tsarsprocket.reportmid.viewStateApi.visualizer.handleUnknownState
import javax.inject.Inject

@PerApi
class LandingVisualizer @Inject constructor() : Visualizer {

    @Composable
    override fun Visualize(state: ViewState, stateHolder: ViewStateHolder) {
        return when(state) {
            is InternalLandingViewState -> {
                when(state) {
                    is DataDragonNotLoadedViewState -> DataDragonNotLoaded()
                    is LandingPageViewState -> Landing()
                }
            }

            else -> handleUnknownState(state)
        }
    }

    @Composable
    private fun DataDragonNotLoaded() {
        TODO("Not yet implemented")
    }

    @Composable
    private fun Landing() {
        LandingScreen()
    }
}