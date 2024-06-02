package com.tsarsprocket.reportmid.viewStateApi.view_state

import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface ViewStateHolder {
    val coroutineScope: CoroutineScope
    val viewStates: StateFlow<ViewState>
    fun createSubholder(initialState: ViewState): ViewStateHolder
    fun postIntent(intent: ViewIntent, goBackState: ViewState? = null)
    fun postEffect(effect: ViewEffect)
    @Composable
    fun Visualize()
}