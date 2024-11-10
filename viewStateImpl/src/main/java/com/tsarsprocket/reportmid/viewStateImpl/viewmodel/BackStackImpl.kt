package com.tsarsprocket.reportmid.viewStateImpl.viewmodel

import com.tsarsprocket.reportmid.viewStateApi.viewState.BackStack
import com.tsarsprocket.reportmid.viewStateApi.viewState.BackStack.Entry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class BackStackImpl : BackStack {

    private val stack = mutableListOf<Entry>()
    private val stackSizePublisher = MutableStateFlow(stack.size)

    override val stackSize = stackSizePublisher.asStateFlow()

    override fun pop(): Entry? = stack.removeLastOrNull().also { stackSizePublisher.value = stack.size }

    override fun push(entry: Entry) {
        stack.add(entry)
        stackSizePublisher.value = stack.size
    }

}