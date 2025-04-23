package com.tsarsprocket.reportmid.summonerViewImpl.stateInitializer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.summonerViewApi.navigation.SummonerViewNavigation
import com.tsarsprocket.reportmid.summonerViewImpl.viewState.SummonerViewState
import com.tsarsprocket.reportmid.viewStateApi.navigation.Navigation
import com.tsarsprocket.reportmid.viewStateApi.stateInitializer.ViewStateInitializer
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import javax.inject.Inject

@PerApi
class SummonerViewStateInitializer @Inject constructor(
    @Navigation(SummonerViewNavigation.TAG)
    private val navigation: SummonerViewNavigation
) : ViewStateInitializer {

    override fun initialize(state: ViewState, holder: ViewStateHolder) {
        (state as? SummonerViewState)?.run {
            with(navigation) { profileOverviewStateHolder.startProfileOverview(summonerPuuid, summonerRegion) }
        }
    }
}
