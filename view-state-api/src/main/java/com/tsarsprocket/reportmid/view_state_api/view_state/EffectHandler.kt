package com.tsarsprocket.reportmid.view_state_api.view_state

import androidx.fragment.app.Fragment

interface EffectHandler<Effect : ViewEffect> {
    suspend fun handleEffect(effect: Effect, fragment: Fragment, controller: ViewStateHolder)
}