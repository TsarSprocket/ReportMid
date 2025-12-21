package com.tsarsprocket.reportmid.summonerViewImpl.stateInitializer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.summonerViewApi.navigation.StartSummonerViewNavigation
import com.tsarsprocket.reportmid.summonerViewImpl.viewState.InternalSummonerViewState
import com.tsarsprocket.reportmid.viewStateApi.navigation.Navigation
import com.tsarsprocket.reportmid.viewStateApi.stateInitializer.ViewStateInitializer
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import javax.inject.Inject

@PerApi
internal class SummonerViewStateInitializer @Inject constructor(
    @param:Navigation(StartSummonerViewNavigation.TAG)
    private val navigation: StartSummonerViewNavigation
) : ViewStateInitializer {

    override fun initialize(state: ViewState, holder: ViewStateHolder) {
        (state as? InternalSummonerViewState)?.run {
            with(navigation) { profileOverviewStateHolder.startProfileOverview(summonerPuuid, summonerRegion) }
        }
    }
}
