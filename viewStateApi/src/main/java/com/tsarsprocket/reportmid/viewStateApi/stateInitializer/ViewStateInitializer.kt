package com.tsarsprocket.reportmid.viewStateApi.stateInitializer

import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder

interface ViewStateInitializer {
    fun initialize(state: ViewState, holder: ViewStateHolder)
}