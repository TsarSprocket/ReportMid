package com.tsarsprocket.reportmid.matchDetails.impl.visualizer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Visualizer
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchDetails.api.viewIntent.MatchDetailsIntent
import com.tsarsprocket.reportmid.matchDetails.impl.view.ErrorNotLoaded
import com.tsarsprocket.reportmid.matchDetails.impl.view.Loading
import com.tsarsprocket.reportmid.matchDetails.impl.view.MatchDetails
import com.tsarsprocket.reportmid.matchDetails.impl.viewState.AbstractMatchDetailsState
import com.tsarsprocket.reportmid.matchDetails.impl.viewState.LoadingState
import com.tsarsprocket.reportmid.matchDetails.impl.viewState.MatchDetailsState
import com.tsarsprocket.reportmid.matchDetails.impl.viewState.NotLoadedState
import com.tsarsprocket.reportmid.summonerViewApi.navigation.OngoingSummonerViewNavigation
import com.tsarsprocket.reportmid.viewStateApi.navigation.Navigation
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.visualizer.ViewStateVisualizer
import javax.inject.Inject

@PerApi
@Visualizer
internal class MatchDetailsVisualizer @Inject constructor(
    @param:Navigation(OngoingSummonerViewNavigation.TAG)
    private val navigation: OngoingSummonerViewNavigation
) : ViewStateVisualizer {

    @Composable
    override fun Visualize(
        modifier: Modifier,
        state: ViewState,
        stateHolder: ViewStateHolder
    ) {
        if(state is AbstractMatchDetailsState) {
            with(stateHolder) {
                when(state) {
                    is MatchDetailsState -> ShowMatchDetails(modifier, state)
                    is LoadingState -> ShowLoading(modifier, state)
                    is NotLoadedState -> ShowNotLoaded(modifier, state)
                }
            }
        } else {
            super.Visualize(modifier, state, stateHolder)
        }
    }

    fun ViewStateHolder.navigateToSummoner(puuid: String, region: Region) {
        with(navigation) { showProfileForSummoner(summonerPuuid = puuid, summonerRegion = region) }
    }

    @Composable
    fun ShowLoading(modifier: Modifier, state: LoadingState) {
        Loading(modifier)
    }

    @Composable
    fun ViewStateHolder.ShowMatchDetails(modifier: Modifier, state: MatchDetailsState) {
        MatchDetails(
            modifier = modifier,
            state = state,
            onPlayerClick = { puuid -> navigateToSummoner(puuid, state.region) }
        )
    }

    @Composable
    fun ViewStateHolder.ShowNotLoaded(modifier: Modifier, state: NotLoadedState) {
        ErrorNotLoaded(modifier = modifier) {
            postIntent(MatchDetailsIntent(matchId = state.matchId, region = state.region))
        }
    }
}