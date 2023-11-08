package com.tsarsprocket.reportmid.app.di

import android.content.Context
import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.view_state_api.view_state.EffectHandler
import com.tsarsprocket.reportmid.view_state_api.view_state.StateReducer
import com.tsarsprocket.reportmid.view_state_api.view_state.StateVisualizer
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewEffect
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewIntent
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewState
import kotlinx.coroutines.CoroutineDispatcher

class AppApiLazyProxy(appApiProducer: () -> AppApi) : AppApi {

    private val appApi by lazy(appApiProducer)

    override fun getAppContext(): Context = appApi.getAppContext()

    override fun getCollectedStateReducers(): Map<Class<out ViewIntent>, StateReducer<*>> = appApi.getCollectedStateReducers()

    override fun getCollectedStateVisualizers(): Map<Class<out ViewState>, StateVisualizer<*>> = appApi.getCollectedStateVisualizers()

    override fun getCollectedEffectHandlers(): Map<Class<out ViewEffect>, EffectHandler<*>> = appApi.getCollectedEffectHandlers()

    override fun getUiDespatcher(): CoroutineDispatcher = appApi.getUiDespatcher()

    override fun getIoDispatcher(): CoroutineDispatcher = appApi.getIoDispatcher()

    override fun getComputationDispatcher(): CoroutineDispatcher = appApi.getComputationDispatcher()
}