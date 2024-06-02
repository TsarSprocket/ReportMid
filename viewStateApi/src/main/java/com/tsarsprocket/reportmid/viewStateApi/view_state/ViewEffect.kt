package com.tsarsprocket.reportmid.viewStateApi.view_state

import androidx.fragment.app.Fragment

interface ViewEffect {
    suspend fun handle(fragment: Fragment, stateHolder: ViewStateHolder)
}
