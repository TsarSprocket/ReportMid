package com.tsarsprocket.reportmid.profileOverviewImpl.visualizer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Visualizer
import com.tsarsprocket.reportmid.profileOverviewApi.viewIntent.ProfileOverviewViewIntent
import com.tsarsprocket.reportmid.profileOverviewImpl.view.ProfileOverview
import com.tsarsprocket.reportmid.profileOverviewImpl.view.ProfileOverviewError
import com.tsarsprocket.reportmid.profileOverviewImpl.view.ProfileOverviewLoading
import com.tsarsprocket.reportmid.profileOverviewImpl.viewState.ProfileOverviewStateCluster
import com.tsarsprocket.reportmid.profileOverviewImpl.viewState.ProfileOverviewStateCluster.LoadingState
import com.tsarsprocket.reportmid.profileOverviewImpl.viewState.ProfileOverviewStateCluster.ProfileState
import com.tsarsprocket.reportmid.profileOverviewImpl.viewState.ProfileOverviewStateCluster.ShowErrorState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.visualizer.ViewStateVisualizer
import javax.inject.Inject

@PerApi
@Visualizer
internal class ProfileOverviewVisualizer @Inject constructor() : ViewStateVisualizer {

    @Composable
    override fun Visualize(modifier: Modifier, state: ViewState, stateHolder: ViewStateHolder) = if(state is ProfileOverviewStateCluster) {
        when(state) {
            is ShowErrorState -> stateHolder.ShowProfileOverviewError(modifier, state)
            is LoadingState -> ProfileOverviewLoading(modifier)
            is ProfileState -> ProfileOverview(modifier, state)
        }
    } else super.Visualize(modifier, state, stateHolder)

    @Composable
    private fun ViewStateHolder.ShowProfileOverviewError(modifier: Modifier, state: ShowErrorState) {
        ProfileOverviewError(modifier) {
            postIntent(ProfileOverviewViewIntent(state.summonerPuuid, state.summonerRegion, isRetry = true))
        }
    }

}