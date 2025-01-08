package com.tsarsprocket.reportmid.profileScreenImpl.viewIntent

import com.tsarsprocket.reportmid.profileScreenApi.viewIntent.ShowProfileScreenViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder
import javax.inject.Inject

internal class ShowProfileScreenViewIntentImpl @Inject constructor() : ShowProfileScreenViewIntent {

    override suspend fun reduce(state: ViewState, stateHolder: ViewStateHolder): ViewState {
        TODO("Not yet implemented")
    }
}