package com.tsarsprocket.reportmid.viewStateApi.viewState

import kotlinx.coroutines.flow.StateFlow

interface BackStack {
    val stackSize: StateFlow<Int>
    fun pop(): Entry?
    fun push(entry: Entry)

    data class Entry(
        val holder: ViewStateHolder,
        val state: ViewState,
    )
}