package com.tsarsprocket.reportmid.landingImpl.viewState

import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import kotlinx.parcelize.Parcelize

internal sealed interface InternalLandingViewState : ViewState {

    @Parcelize
    data object DataDragonNotLoadedViewState : InternalLandingViewState


    @Parcelize
    data object LandingPageViewState : InternalLandingViewState
}