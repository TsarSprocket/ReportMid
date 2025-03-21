package com.tsarsprocket.reportmid.landingImpl.viewState

import com.tsarsprocket.reportmid.kspApi.annotation.State
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import kotlinx.parcelize.Parcelize

internal sealed interface InternalLandingViewState : ViewState {

    @Parcelize
    @State
    data class DataDragonNotLoadedViewState(val isLoading: Boolean) : InternalLandingViewState


    @Parcelize
    @State
    data object LandingPageViewState : InternalLandingViewState
}