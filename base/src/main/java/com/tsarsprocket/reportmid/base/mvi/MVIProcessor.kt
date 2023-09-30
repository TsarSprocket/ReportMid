package com.tsarsprocket.reportmid.base.mvi

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class MVIProcessor<State, Intent, ViewState, ViewEffect>(
    initialState: State,
    initialViewState: ViewState,
    private val reducer: ((State, Intent, MVIProcessor<State, Intent, ViewState, ViewEffect>) -> Reduction<State, ViewState>),
) : MVIStore<ViewState, ViewEffect, Intent> {

    private var state: State = initialState
    private val viewStatePublisher: MutableStateFlow<ViewState> = MutableStateFlow(initialViewState)
    private val viewEffectPublisher: MutableSharedFlow<ViewEffect> = MutableSharedFlow()

    override val viewEffects: SharedFlow<ViewEffect> = viewEffectPublisher.asSharedFlow()
    override val viewStates: StateFlow<ViewState> = viewStatePublisher.asStateFlow()

    override fun processIntent(intent: Intent) {
        val (newState, viewState) = reducer(state, intent, this)
        if (viewState != null) viewStatePublisher.value = viewState
        state = newState
    }

    suspend fun postViewEffect(viewEffect: ViewEffect) {
        viewEffectPublisher.emit(viewEffect)
    }
}