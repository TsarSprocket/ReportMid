package com.tsarsprocket.reportmid.landingImpl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Reducer
import com.tsarsprocket.reportmid.landingApi.navigation.LandingNavigation
import com.tsarsprocket.reportmid.landingApi.viewIntent.LandingIntent
import com.tsarsprocket.reportmid.landingApi.viewIntent.LandingIntent.LandingStartLoadViewIntent
import com.tsarsprocket.reportmid.landingApi.viewIntent.LandingIntent.QuitViewIntent
import com.tsarsprocket.reportmid.landingApi.viewIntent.LandingIntent.SummonerFoundViewIntent
import com.tsarsprocket.reportmid.landingImpl.domain.LandingUseCase
import com.tsarsprocket.reportmid.landingImpl.viewIntent.InternalLandingIntent
import com.tsarsprocket.reportmid.landingImpl.viewIntent.InternalLandingIntent.DataDragonNotLoadedViewIntent
import com.tsarsprocket.reportmid.landingImpl.viewIntent.InternalLandingIntent.TryReinitialize
import com.tsarsprocket.reportmid.landingImpl.viewState.InternalLandingViewState.DataDragonNotLoadedViewState
import com.tsarsprocket.reportmid.landingImpl.viewState.InternalLandingViewState.LandingPageViewState
import com.tsarsprocket.reportmid.utils.common.STANDARD_FIRST_DELAY_MILLIS
import com.tsarsprocket.reportmid.utils.common.STANDARD_RETRY_DELAY_MILLIS
import com.tsarsprocket.reportmid.utils.common.logError
import com.tsarsprocket.reportmid.utils.coroutines.doAtLeast
import com.tsarsprocket.reportmid.viewStateApi.navigation.Navigation
import com.tsarsprocket.reportmid.viewStateApi.reducer.ViewStateReducer
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.QuitViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@PerApi
@Reducer(
    explicitIntents = [
        LandingStartLoadViewIntent::class,
        SummonerFoundViewIntent::class,
        QuitViewIntent::class,
    ],
)
internal class LandingReducer @Inject constructor(
    private val useCase: LandingUseCase,
    @Navigation(LandingNavigation.TAG)
    private val navigation: LandingNavigation,
) : ViewStateReducer {

    override suspend fun reduce(intent: ViewIntent, state: ViewState, stateHolder: ViewStateHolder): ViewState = when(intent) {
        is LandingIntent -> {
            when(intent) {
                is LandingStartLoadViewIntent -> {
                    stateHolder.startLoading(STANDARD_FIRST_DELAY_MILLIS); LandingPageViewState
                }

                is SummonerFoundViewIntent -> stateHolder.summonerFound(intent, state)
                is QuitViewIntent -> stateHolder.quit(state)
            }
        }

        is InternalLandingIntent -> {
            when(intent) {
                is DataDragonNotLoadedViewIntent -> DataDragonNotLoadedViewState(isLoading = false)
                is TryReinitialize -> {
                    stateHolder.startLoading(STANDARD_RETRY_DELAY_MILLIS); DataDragonNotLoadedViewState(isLoading = true)
                }
            }
        }

        else -> super.reduce(intent, state, stateHolder)
    }

    private fun ViewStateHolder.startLoading(minDelay: Long) {
        viewHolderScope.launch {
            val isInitialized = doAtLeast(minDelay.milliseconds) {
                try {
                    useCase.initializeDataDragon()
                    true
                } catch(exception: Exception) {
                    logError("Failed to initialize Data Dragon", exception)
                    false
                }
            }

            if(isInitialized) {
                useCase.getExistingAccountPuuidAndRegion()?.let { (puuid, region) ->
                    with(navigation) { proceed(puuid, region) }
                } ?: with(navigation) { findSummoner() }
            } else {
                postIntent(DataDragonNotLoadedViewIntent)
            }
        }
    }

    private suspend fun ViewStateHolder.summonerFound(intent: SummonerFoundViewIntent, state: ViewState): ViewState {
        coroutineScope {
            launch {
                useCase.createAccount(intent.puuid, intent.region)
                with(navigation) { proceed(intent.puuid, intent.region) }
            }
        }
        return state
    }

    private fun ViewStateHolder.quit(state: ViewState): ViewState = postEffect(QuitViewEffect).run { state }
}
