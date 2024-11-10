package com.tsarsprocket.reportmid.findSummonerImpl.viewEffect

import androidx.annotation.StringRes
import com.tsarsprocket.reportmid.viewStateApi.view.ViewStateFragment
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.ViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder

internal class ShowSnackViewEffect(@StringRes val text: Int) : ViewEffect {

    override suspend fun handle(fragment: ViewStateFragment, stateHolder: ViewStateHolder) {
        fragment.snackbarHostState.showSnackbar(fragment.getString(text))
    }
}