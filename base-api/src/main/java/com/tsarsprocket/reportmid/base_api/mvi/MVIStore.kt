package com.tsarsprocket.reportmid.base_api.mvi

import kotlinx.coroutines.flow.Flow

interface MVIStore<ViewState, ViewEffect, Intent> {
    val viewEffects: Flow<ViewEffect>
    val viewStates: Flow<ViewState>
    fun processIntent(intent: Intent)
}