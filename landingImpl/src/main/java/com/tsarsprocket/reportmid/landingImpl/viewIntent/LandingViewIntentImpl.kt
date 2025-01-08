package com.tsarsprocket.reportmid.landingImpl.viewIntent

import com.tsarsprocket.reportmid.landingApi.viewIntent.LandingViewIntent
import com.tsarsprocket.reportmid.landingImpl.di.LandingCapabilityProvisionModule
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder
import kotlinx.parcelize.Parcelize

internal sealed class LandingViewIntentImpl : LandingViewIntent {

    private val reducer by lazy { LandingCapabilityProvisionModule.landingCapabilityComponent.getLandingStateReducer() }

    override suspend fun reduce(state: ViewState, stateHolder: ViewStateHolder): ViewState {
        return reducer.reduce(this, stateHolder)
    }

    @Parcelize
    data object DataDragonNotLoadedViewIntent : LandingViewIntentImpl()

    @Parcelize
    data object LandingStartLoadIntent : LandingViewIntentImpl()
}