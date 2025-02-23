package com.tsarsprocket.reportmid.profileScreenImpl.visualizer

import androidx.compose.runtime.Composable
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.profileScreenImpl.viewState.ProfileScreenViewState
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.visualizer.Visualizer
import javax.inject.Inject

@PerApi
internal class ProfileScreenVisualizer @Inject constructor() : Visualizer {

    @Composable
    override fun Visualize(state: ViewState, stateHolder: ViewStateHolder) = ReportMidTheme {
        if(state is ProfileScreenViewState) {
            ShowProfile()
        } else super.Visualize(state, stateHolder)
    }

    @Composable
    private fun ShowProfile() {
        TODO("Not yet implemented")
    }
}