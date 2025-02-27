package com.tsarsprocket.reportmid.appImpl.di

import androidx.fragment.app.Fragment
import com.tsarsprocket.reportmid.baseApi.di.AppScope
import com.tsarsprocket.reportmid.baseApi.di.BindingExport
import com.tsarsprocket.reportmid.baseApi.di.FragmentsCreator
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Aggregated
import com.tsarsprocket.reportmid.viewStateApi.di.EffectHandlerBinding
import com.tsarsprocket.reportmid.viewStateApi.di.ReducerBinding
import com.tsarsprocket.reportmid.viewStateApi.di.VisualizerBinding
import com.tsarsprocket.reportmid.viewStateApi.effectHandler.ViewEffectHandler
import com.tsarsprocket.reportmid.viewStateApi.reducer.ViewStateReducer
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.ViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.visualizer.ViewStateVisualizer
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
internal class AggregatorModule {

    @Provides
    @AppScope
    @Aggregated
    fun provideFragmentCreators(@BindingExport bindingExports: @JvmSuppressWildcards Set<Any>): Map<Class<out Fragment>, Provider<Fragment>> {
        return bindingExports.filterIsInstance<FragmentsCreator>()
            .fold(emptyMap()) { acc, entry -> acc + entry.getFragmentCreators() }
    }

    @Provides
    @AppScope
    @Aggregated
    fun provideViewStateReducers(@BindingExport bindingExports: @JvmSuppressWildcards Set<Any>): Map<Class<out ViewIntent>, Provider<ViewStateReducer>> {
        return bindingExports.filterIsInstance<ReducerBinding>()
            .fold(emptyMap()) { acc, entry -> acc + entry.getReducers() }
    }

    @Provides
    @AppScope
    @Aggregated
    fun provideVisualizers(@BindingExport bindingExports: @JvmSuppressWildcards Set<Any>): Map<Class<out ViewState>, Provider<ViewStateVisualizer>> {
        return bindingExports.filterIsInstance<VisualizerBinding>()
            .fold(emptyMap()) { acc, entry -> acc + entry.getVisualizers() }
    }

    @Provides
    @AppScope
    @Aggregated
    fun provideEffectHandlers(@BindingExport bindingExports: @JvmSuppressWildcards Set<Any>): Map<Class<out ViewEffect>, Provider<ViewEffectHandler>> {
        return bindingExports.filterIsInstance<EffectHandlerBinding>()
            .fold(emptyMap()) { acc, entry -> acc + entry.getEffectHandlers() }
    }
}