package com.tsarsprocket.reportmid.viewStateApi.effectHandler

import com.tsarsprocket.reportmid.utils.common.logError
import com.tsarsprocket.reportmid.viewStateApi.view.ViewStateFragment
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.ViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder

interface EffectHandler {

    suspend fun handle(effect: ViewEffect, fragment: ViewStateFragment, stateHolder: ViewStateHolder): Any? {
        return logError("Unknown view effect of class ${effect::class.simpleName}")
    }
}