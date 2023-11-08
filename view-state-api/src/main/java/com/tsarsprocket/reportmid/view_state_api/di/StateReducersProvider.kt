package com.tsarsprocket.reportmid.view_state_api.di

import com.tsarsprocket.reportmid.view_state_api.view_state.StateReducer
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewIntent

interface StateReducersProvider {
    fun getStateReducersMap(): Map<Class<out ViewIntent>, @JvmSuppressWildcards StateReducer<*>>
}