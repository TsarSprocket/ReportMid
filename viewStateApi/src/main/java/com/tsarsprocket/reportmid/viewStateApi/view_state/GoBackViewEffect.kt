package com.tsarsprocket.reportmid.viewStateApi.view_state

import androidx.fragment.app.Fragment

object GoBackViewEffect : ViewEffect {

    override suspend fun handle(fragment: Fragment, stateHolder: ViewStateHolder) {
        fragment.activity?.onBackPressed()
    }
}