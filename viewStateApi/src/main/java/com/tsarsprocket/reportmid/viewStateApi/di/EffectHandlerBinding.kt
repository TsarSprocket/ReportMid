package com.tsarsprocket.reportmid.viewStateApi.di

import com.tsarsprocket.reportmid.viewStateApi.effectHandler.ViewEffectHandler
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.ViewEffect
import javax.inject.Provider

interface EffectHandlerBinding {
    fun getEffectHandlers(): Map<Class<out ViewEffect>, @JvmSuppressWildcards Provider<ViewEffectHandler>>
}