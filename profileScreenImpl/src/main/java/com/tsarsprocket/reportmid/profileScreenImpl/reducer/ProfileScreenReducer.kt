package com.tsarsprocket.reportmid.profileScreenImpl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.profileScreenApi.viewIntent.ShowProfileScreenViewIntent
import com.tsarsprocket.reportmid.viewStateApi.reducer.Reducer
import com.tsarsprocket.reportmid.viewStateApi.reducer.handleUnknownIntent
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder
import javax.inject.Inject

@PerApi
internal class ProfileScreenReducer @Inject constructor() : Reducer {

    override suspend fun reduce(intent: ViewIntent, state: ViewState, stateHolder: ViewStateHolder): ViewState {
        return when(intent) {
            is ShowProfileScreenViewIntent -> showProfile()
            else -> handleUnknownIntent(intent, state)
        }
    }

    private fun showProfile(): ViewState {
        TODO("Not yet implemented")
    }
}