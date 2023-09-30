package com.tsarsprocket.reportmid.landing_impl.viewmodel

import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.base.mvi.MVIProcessor
import com.tsarsprocket.reportmid.base.mvi.Reduction
import com.tsarsprocket.reportmid.landing_impl.state.ViewEffect
import com.tsarsprocket.reportmid.landing_impl.state.ViewIntent
import com.tsarsprocket.reportmid.landing_impl.state.ViewState
import com.tsarsprocket.reportmid.landing_impl.usecase.LandingUseCase
import javax.inject.Inject

class LandingViewModel @Inject constructor(
    private val landingUseCase: LandingUseCase
) : ViewModel() {

    private val processor = MVIProcessor<State, ViewIntent, ViewState, ViewEffect>(State, ViewState, this::reduce)
    val states = processor.viewStates
    val effects = processor.viewEffects

    private fun reduce(state: State, intent: ViewIntent, processor: MVIProcessor<State, ViewIntent, ViewState, ViewEffect>): Reduction<State, ViewState> {
        TODO()
    }

    object State
}