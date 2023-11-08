package com.tsarsprocket.reportmid.view_state_impl.view_model

import com.tsarsprocket.reportmid.view_state_api.view_state.ViewState

internal class BackStack {

    private val stack = mutableListOf<ViewState>()

    fun pop(): ViewState? = stack.removeLastOrNull()

    fun push(viewState: ViewState) {
        stack.add(viewState)
    }
}