package com.tsarsprocket.reportmid.mainScreenImpl.stateInitializer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.mainScreenApi.navigation.MainScreenNavigation
import com.tsarsprocket.reportmid.mainScreenImpl.viewState.MainScreenViewState
import com.tsarsprocket.reportmid.viewStateApi.navigation.Navigation
import com.tsarsprocket.reportmid.viewStateApi.stateInitializer.ViewStateInitializer
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import javax.inject.Inject

@PerApi
class MainScreenStateInitializer @Inject constructor(
    @Navigation(MainScreenNavigation.TAG)
    private val navigation: MainScreenNavigation
) : ViewStateInitializer {

    override fun initialize(state: ViewState, holder: ViewStateHolder) {
        (state as? MainScreenViewState)?.run {
            with(navigation) { summonerStateHolder.startSummonerView(initialSummonerPuuid, initialSummonerRegion) }
        }
    }
}