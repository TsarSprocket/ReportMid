package com.tsarsprocket.reportmid.viewStateApi.viewEffect

import com.tsarsprocket.reportmid.viewStateApi.view.ViewStateFragment
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder

interface ViewEffect {
    suspend fun handle(fragment: ViewStateFragment, stateHolder: ViewStateHolder)
}
