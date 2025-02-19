package com.tsarsprocket.reportmid.viewStateImpl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.viewStateApi.reducer.Reducer
import com.tsarsprocket.reportmid.viewStateApi.reducer.handleUnknownIntent
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.QuitViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.QuitViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder
import javax.inject.Inject

@PerApi
class DefaultReducer @Inject constructor() : Reducer {

    override suspend fun reduce(intent: ViewIntent, state: ViewState, stateHolder: ViewStateHolder): ViewState {
        return when(intent) {
            QuitViewIntent -> stateHolder.quit().run { state }
            else -> handleUnknownIntent(intent, state)
        }
    }

    private fun ViewStateHolder.quit() {
        postEffect(QuitViewEffect)
    }
}