package com.tsarsprocket.reportmid.findSummonerImpl.effectHandler

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.findSummonerImpl.viewEffect.ShowSnackViewEffect
import com.tsarsprocket.reportmid.kspApi.annotation.EffectHandler
import com.tsarsprocket.reportmid.viewStateApi.effectHandler.ViewEffectHandler
import com.tsarsprocket.reportmid.viewStateApi.view.ViewStateFragment
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.ViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import javax.inject.Inject

@PerApi
@EffectHandler
class FindSummonerEffectHandler @Inject constructor() : ViewEffectHandler {

    override suspend fun handle(effect: ViewEffect, fragment: ViewStateFragment, stateHolder: ViewStateHolder) = when(effect) {
        is ShowSnackViewEffect -> with(fragment) { snackbarHostState.showSnackbar(getString(effect.text)) }
        else -> super.handle(effect, fragment, stateHolder)
    }
}