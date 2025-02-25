package com.tsarsprocket.reportmid.findSummonerImpl.effectHandler

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.findSummonerImpl.viewEffect.ShowSnackViewEffect
import com.tsarsprocket.reportmid.viewStateApi.effectHandler.EffectHandler
import com.tsarsprocket.reportmid.viewStateApi.view.ViewStateFragment
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.ViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder
import javax.inject.Inject

@PerApi
class FindSummonerEffectHandler @Inject constructor() : EffectHandler {

    override suspend fun handle(effect: ViewEffect, fragment: ViewStateFragment, stateHolder: ViewStateHolder) = when(effect) {
        is ShowSnackViewEffect -> with(fragment) { snackbarHostState.showSnackbar(getString(effect.text)) }
        else -> super.handle(effect, fragment, stateHolder)
    }
}