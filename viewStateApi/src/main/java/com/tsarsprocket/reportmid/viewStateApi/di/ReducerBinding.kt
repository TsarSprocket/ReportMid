package com.tsarsprocket.reportmid.viewStateApi.di

import com.tsarsprocket.reportmid.viewStateApi.reducer.Reducer
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import javax.inject.Provider

interface ReducerBinding {
    fun getReducers(): Map<Class<out ViewIntent>, @JvmSuppressWildcards Provider<Reducer>>
}
