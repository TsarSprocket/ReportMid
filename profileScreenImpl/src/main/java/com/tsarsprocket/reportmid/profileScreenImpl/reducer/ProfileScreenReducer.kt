package com.tsarsprocket.reportmid.profileScreenImpl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Reducer
import com.tsarsprocket.reportmid.profileScreenApi.viewIntent.ShowProfileScreenViewIntent
import com.tsarsprocket.reportmid.viewStateApi.reducer.ViewStateReducer
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import javax.inject.Inject

@PerApi
@Reducer(
    explicitIntents = [
        ShowProfileScreenViewIntent::class,
    ],
)
internal class ProfileScreenReducer @Inject constructor() : ViewStateReducer {

    override suspend fun reduce(intent: ViewIntent, state: ViewState, stateHolder: ViewStateHolder): ViewState = when(intent) {
        is ShowProfileScreenViewIntent -> showProfile()
        else -> super.reduce(intent, state, stateHolder)
    }

    private fun showProfile(): ViewState {
        TODO("Not yet implemented")
    }
}