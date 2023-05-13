package com.tsarsprocket.reportmid.base.mvi

import com.tsarsprocket.reportmid.utils.flow.hide
import kotlinx.coroutines.flow.*

class MVIProcessor<State, Intent, ViewState, ViewEffect>(
    private val reducer: MVIReducer<State, Intent, ViewState, ViewEffect>,
    initialState: State,
    initialViewState: ViewState,
) : MVIStore<ViewState, ViewEffect, Intent> {

    override val viewEffects: SharedFlow<ViewEffect>
    override val viewStates: StateFlow<ViewState>

    private var state: State = initialState
    private val viewStatePublisher: MutableStateFlow<ViewState> = MutableStateFlow(initialViewState)
    private val viewEffectPublisher: MutableSharedFlow<ViewEffect> = MutableSharedFlow()

    init {
        viewEffects = viewEffectPublisher.hide()
        viewStates = viewStatePublisher.hide()
    }
    
    override fun postIntent(intent: Intent) {
        state = reducer(state, intent, this)
    }

    fun postViewState(viewState: ViewState) {
        viewStatePublisher.value = viewState
    }

    suspend fun postViewEffect(viewEffect: ViewEffect) {
        viewEffectPublisher.emit(viewEffect)
    }
}