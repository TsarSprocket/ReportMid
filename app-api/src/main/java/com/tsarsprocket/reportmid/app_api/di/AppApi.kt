package com.tsarsprocket.reportmid.app_api.di

import android.content.Context
import com.tsarsprocket.reportmid.base.di.Api
import com.tsarsprocket.reportmid.base.di.qualifiers.Computation
import com.tsarsprocket.reportmid.base.di.qualifiers.Io
import com.tsarsprocket.reportmid.base.di.qualifiers.Ui
import com.tsarsprocket.reportmid.view_state_api.di.ViewStateCollected
import com.tsarsprocket.reportmid.view_state_api.view_state.EffectHandler
import com.tsarsprocket.reportmid.view_state_api.view_state.StateReducer
import com.tsarsprocket.reportmid.view_state_api.view_state.StateVisualizer
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewEffect
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewIntent
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewState
import kotlinx.coroutines.CoroutineDispatcher

interface AppApi : Api {
    fun getAppContext(): Context
    @ViewStateCollected
    fun getCollectedStateReducers(): Map<Class<out ViewIntent>, @JvmSuppressWildcards StateReducer<*>>
    @ViewStateCollected
    fun getCollectedStateVisualizers(): Map<Class<out ViewState>, @JvmSuppressWildcards StateVisualizer<*>>
    @ViewStateCollected
    fun getCollectedEffectHandlers(): Map<Class<out ViewEffect>, @JvmSuppressWildcards EffectHandler<*>>
    @Ui
    fun getUiDespatcher(): CoroutineDispatcher
    @Io
    fun getIoDispatcher(): CoroutineDispatcher
    @Computation
    fun getComputationDispatcher(): CoroutineDispatcher
}