package com.tsarsprocket.reportmid.viewStateApi.viewEffect

import com.tsarsprocket.reportmid.viewStateApi.view.ViewStateFragment
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder

object GoBackViewEffect : ViewEffect {

    override suspend fun handle(fragment: ViewStateFragment, stateHolder: ViewStateHolder) {
        fragment.activity?.onBackPressed()
    }
}