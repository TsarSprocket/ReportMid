package com.tsarsprocket.reportmid.viewStateImpl.effectHandler

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.EffectHandler
import com.tsarsprocket.reportmid.viewStateApi.effectHandler.ViewEffectHandler
import com.tsarsprocket.reportmid.viewStateApi.view.ViewStateFragment
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.GoBackViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.QuitViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.ViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder
import javax.inject.Inject

@PerApi
@EffectHandler(
    explicitEffects = [
        GoBackViewEffect::class,
        QuitViewEffect::class,
    ],
)
class DefaultEffectHandler @Inject constructor() : ViewEffectHandler {

    override suspend fun handle(effect: ViewEffect, fragment: ViewStateFragment, stateHolder: ViewStateHolder) = when(effect) {
        is GoBackViewEffect -> fragment.activity?.onBackPressed()
        is QuitViewEffect -> fragment.activity?.finishAndRemoveTask()
        else -> super.handle(effect, fragment, stateHolder)
    }
}