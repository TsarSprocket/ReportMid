package com.tsarsprocket.reportmid.viewStateImpl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Reducer
import com.tsarsprocket.reportmid.viewStateApi.reducer.ViewStateReducer
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.QuitViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.GeneralPurposeViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.LazyViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.QuitViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ShowStateViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateImpl.viewState.LazyViewState
import javax.inject.Inject

@PerApi
@Reducer(
    explicitIntents = [
        LazyViewIntent::class,
        QuitViewIntent::class,
        ShowStateViewIntent::class,
    ],
)
class GeneralPurposeReducer @Inject constructor() : ViewStateReducer {

    override suspend fun reduce(intent: ViewIntent, state: ViewState, stateHolder: ViewStateHolder): ViewState {
        return if(intent is GeneralPurposeViewIntent) {
            when(intent) {
                QuitViewIntent -> stateHolder.quit().run { state }
                is LazyViewIntent -> LazyViewState(intent.intent)
                is ShowStateViewIntent -> intent.state
            }
        } else {
            super.reduce(intent, state, stateHolder)
        }
    }

    private fun ViewStateHolder.quit() {
        postEffect(QuitViewEffect)
    }
}
