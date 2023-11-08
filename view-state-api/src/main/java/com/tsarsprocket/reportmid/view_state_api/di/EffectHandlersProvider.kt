package com.tsarsprocket.reportmid.view_state_api.di

import com.tsarsprocket.reportmid.view_state_api.view_state.EffectHandler
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewEffect

interface EffectHandlersProvider {
    fun getEffectHandlersMap(): Map<Class<out ViewEffect>, @JvmSuppressWildcards EffectHandler<*>>
}