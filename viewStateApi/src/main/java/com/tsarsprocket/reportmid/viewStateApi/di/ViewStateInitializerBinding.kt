package com.tsarsprocket.reportmid.viewStateApi.di

import com.tsarsprocket.reportmid.viewStateApi.stateInitializer.ViewStateInitializer
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import javax.inject.Provider

interface ViewStateInitializerBinding {
    fun getViewStateInitializers(): Map<Class<out ViewState>, @JvmSuppressWildcards Provider<ViewStateInitializer>>
}
