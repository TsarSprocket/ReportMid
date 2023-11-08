package com.tsarsprocket.reportmid.view_state_api.di

import com.tsarsprocket.reportmid.view_state_api.view_state.StateVisualizer
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewState


interface StateVisualizersProvider {
    fun getStateVisualizersMap(): Map<Class<out ViewState>, @JvmSuppressWildcards StateVisualizer<*>>
}