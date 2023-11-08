package com.tsarsprocket.reportmid.view_state_impl.view_model

import com.tsarsprocket.reportmid.view_state_api.view_state.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class BackStack {

    private val stack = mutableListOf<ViewState>()
    private val stackSizePublisher = MutableStateFlow(stack.size)

    val stackSize = stackSizePublisher.asStateFlow()

    fun pop(): ViewState? = stack.removeLastOrNull().also { stackSizePublisher.value = stack.size }

    fun push(viewState: ViewState) {
        stack.add(viewState)
        stackSizePublisher.value = stack.size
    }
}