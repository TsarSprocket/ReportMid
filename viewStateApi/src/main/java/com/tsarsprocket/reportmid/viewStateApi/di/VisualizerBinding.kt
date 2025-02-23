package com.tsarsprocket.reportmid.viewStateApi.di

import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.visualizer.Visualizer
import javax.inject.Provider

interface VisualizerBinding {
    fun getVisualizers(): Map<Class<out ViewState>, @JvmSuppressWildcards Provider<Visualizer>>
}