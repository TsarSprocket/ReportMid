package com.tsarsprocket.reportmid.view_state_impl.view_model

import com.tsarsprocket.reportmid.view_state_api.view_state.ViewState
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewStateHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class BackStack {

    private val stack = mutableListOf<Entry>()
    private val stackSizePublisher = MutableStateFlow(stack.size)

    val stackSize = stackSizePublisher.asStateFlow()

    fun pop(): Entry? = stack.removeLastOrNull().also { stackSizePublisher.value = stack.size }

    fun push(entry: Entry) {
        stack.add(entry)
        stackSizePublisher.value = stack.size
    }

    data class Entry(
        val holder: ViewStateHolder,
        val state: ViewState,
    )
}