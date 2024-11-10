package com.tsarsprocket.reportmid.landingImpl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.findSummonerApi.viewIntent.FindSummonerViewIntent
import com.tsarsprocket.reportmid.landingApi.navigation.LandingRouteOut
import com.tsarsprocket.reportmid.landingImpl.domain.LandingUseCase
import com.tsarsprocket.reportmid.landingImpl.viewIntent.LandingViewIntent
import com.tsarsprocket.reportmid.landingImpl.viewIntent.LandingViewIntent.LandingStartLoadIntent
import com.tsarsprocket.reportmid.landingImpl.viewState.DataDragonNotLoadedViewState
import com.tsarsprocket.reportmid.landingImpl.viewState.LandingViewState
import com.tsarsprocket.reportmid.viewStateApi.navigation.NavigationRoute
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@PerApi
internal class LandingStateReducer @Inject constructor(
    private val useCase: LandingUseCase,
    @NavigationRoute private val routeOut: (@JvmSuppressWildcards LandingRouteOut) -> @JvmSuppressWildcards ViewIntent,
    private val findSummonerIntentFactory: FindSummonerViewIntent.Factory
) {

    suspend fun reduce(intent: LandingViewIntent, stateHolder: ViewStateHolder): ViewState {
        return when(intent) {
            is LandingViewIntent.DataDragonNotLoadedViewIntent -> DataDragonNotLoadedViewState()
            is LandingStartLoadIntent -> startLoading(stateHolder)
        }
    }

    private suspend fun startLoading(stateHolder: ViewStateHolder): ViewState {
        coroutineScope {
            launch {
                try {
                    useCase.initializeDataDragon()
                } catch(exception: Exception) {
                    stateHolder.postIntent(LandingViewIntent.DataDragonNotLoadedViewIntent(this@LandingStateReducer))
                }


                if(useCase.checkAccountExists()) {
                    stateHolder.postIntent(routeOut(LandingRouteOut))
                } else {
                    stateHolder.postIntent(findSummonerIntentFactory.create({ LandingStartLoadIntent(this@LandingStateReducer) }, removeRecentState = true))
                }
            }
        }
        return LandingViewState()
    }
}