package com.tsarsprocket.reportmid.viewStateImpl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Reducer
import com.tsarsprocket.reportmid.viewStateApi.reducer.ViewStateReducer
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.QuitViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.QuitViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import javax.inject.Inject

@PerApi
@Reducer(
    explicitIntents = [
        QuitViewIntent::class,
    ],
)
class DefaultReducer @Inject constructor() : ViewStateReducer {

    override suspend fun reduce(intent: ViewIntent, state: ViewState, stateHolder: ViewStateHolder): ViewState = when(intent) {
        QuitViewIntent -> stateHolder.quit().run { state }
        else -> super.reduce(intent, state, stateHolder)
    }

    private fun ViewStateHolder.quit() {
        postEffect(QuitViewEffect)
    }
}