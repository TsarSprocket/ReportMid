package com.tsarsprocket.reportmid.appImpl.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.baseApi.di.AppScope
import com.tsarsprocket.reportmid.baseApi.di.FragmentCreatorBinding
import com.tsarsprocket.reportmid.baseApi.di.ViewModelFactoryCreatorBinding
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Aggregated
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.BindingExport
import com.tsarsprocket.reportmid.baseApi.viewmodel.ViewModelFactoryCreator
import com.tsarsprocket.reportmid.viewStateApi.di.EffectHandlerBinding
import com.tsarsprocket.reportmid.viewStateApi.di.ReducerBinding
import com.tsarsprocket.reportmid.viewStateApi.di.ViewStateInitializerBinding
import com.tsarsprocket.reportmid.viewStateApi.di.VisualizerBinding
import com.tsarsprocket.reportmid.viewStateApi.effectHandler.ViewEffectHandler
import com.tsarsprocket.reportmid.viewStateApi.reducer.ViewStateReducer
import com.tsarsprocket.reportmid.viewStateApi.stateInitializer.ViewStateInitializer
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
        return bindingExports.filterIsInstance<FragmentCreatorBinding>().aggregate { getFragmentCreators() }
    }

    @Provides
    @AppScope
    @Aggregated
    fun provideViewModelFactoryCreators(@BindingExport bindingExports: @JvmSuppressWildcards Set<Any>): Map<Class<out ViewModel>, Provider<ViewModelFactoryCreator>> {
        return bindingExports.filterIsInstance<ViewModelFactoryCreatorBinding>().aggregate { getViewModelFactoryCreators() }
    }

    @Provides
    @AppScope
    @Aggregated
    fun provideViewStateInitializers(@BindingExport bindingExports: @JvmSuppressWildcards Set<Any>): Map<Class<out ViewState>, Provider<ViewStateInitializer>> {
        return bindingExports.filterIsInstance<ViewStateInitializerBinding>().aggregate { getViewStateInitializers() }
    }

    @Provides
    @AppScope
    @Aggregated
    fun provideViewStateReducers(@BindingExport bindingExports: @JvmSuppressWildcards Set<Any>): Map<Class<out ViewIntent>, Provider<ViewStateReducer>> {
        return bindingExports.filterIsInstance<ReducerBinding>().aggregate { getReducers() }
    }

    @Provides
    @AppScope
    @Aggregated
    fun provideVisualizers(@BindingExport bindingExports: @JvmSuppressWildcards Set<Any>): Map<Class<out ViewState>, Provider<ViewStateVisualizer>> {
        return bindingExports.filterIsInstance<VisualizerBinding>().aggregate { getVisualizer() }
    }

    @Provides
    @AppScope
    @Aggregated
    fun provideEffectHandlers(@BindingExport bindingExports: @JvmSuppressWildcards Set<Any>): Map<Class<out ViewEffect>, Provider<ViewEffectHandler>> {
        return bindingExports.filterIsInstance<EffectHandlerBinding>().aggregate { getEffectHandlers() }
    }

    private fun <I, K, V> Iterable<I>.aggregate(getter: I.() -> Map<Class<out K>, Provider<V>>): Map<Class<out K>, Provider<V>> = flatMap { it.getter().entries }.associate { it.key to it.value }
}