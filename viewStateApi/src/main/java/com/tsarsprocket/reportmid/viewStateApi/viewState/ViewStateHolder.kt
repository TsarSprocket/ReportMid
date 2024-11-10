package com.tsarsprocket.reportmid.viewStateApi.viewState

import androidx.compose.runtime.Composable
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.ViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface ViewStateHolder {
    val coroutineScope: CoroutineScope
    val viewStates: StateFlow<ViewState>
    val currentState: ViewState
    fun createSubholder(initialState: ViewState): ViewStateHolder
    fun pop()
    fun postIntent(intent: ViewIntent)
    fun postEffect(effect: ViewEffect)
    fun push()
    @Composable
    fun Visualize()

    object Preview : ViewStateHolder {
        override val coroutineScope = CoroutineScope(Job())
        override val viewStates = MutableStateFlow(EmptyScreen)
        override val currentState = EmptyScreen

        override fun createSubholder(initialState: ViewState) = this
        override fun postIntent(intent: ViewIntent) {}
        override fun postEffect(effect: ViewEffect) {}
        override fun pop() {}
        override fun push() {}

        @Composable
        override fun Visualize() {
        }
    }
}