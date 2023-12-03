package com.tsarsprocket.reportmid.view_state_api.view_state

import androidx.fragment.app.Fragment

interface ViewEffect {
    suspend fun handle(fragment: Fragment, stateHolder: ViewStateHolder)
}
