package com.tsarsprocket.reportmid.baseApi.mvi

import kotlinx.coroutines.flow.Flow

interface MVIStore<ViewState, ViewEffect, Intent> {
    val viewEffects: Flow<ViewEffect>
    val viewStates: Flow<ViewState>
    fun processIntent(intent: Intent)
}