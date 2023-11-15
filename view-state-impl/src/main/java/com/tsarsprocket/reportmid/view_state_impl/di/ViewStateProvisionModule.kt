package com.tsarsprocket.reportmid.view_state_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base.di.AppScope
import com.tsarsprocket.reportmid.base.di.BindingExport
import com.tsarsprocket.reportmid.view_state_api.di.EffectHandlersProvider
import com.tsarsprocket.reportmid.view_state_api.di.StateReducersProvider
import com.tsarsprocket.reportmid.view_state_api.di.StateVisualizersProvider
import com.tsarsprocket.reportmid.view_state_api.di.ViewStateApi
import com.tsarsprocket.reportmid.view_state_api.view_state.EffectHandler
import com.tsarsprocket.reportmid.view_state_api.view_state.StateReducer
import com.tsarsprocket.reportmid.view_state_api.view_state.StateVisualizer
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewEffect
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewIntent
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewState
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Provider

@Module
interface ViewStateProvisionModule {

    @Binds
    @IntoSet
    @AppScope
    @BindingExport
    fun bindBindingExports(api: ViewStateApi): Any

    companion object {

        internal lateinit var viewStateApiComponent: ViewStateApiComponent
            private set

        @Provides
        @AppScope
        fun provideViewStateApi(appApi: Provider<AppApi>): ViewStateApi {
            return DaggerViewStateApiComponent.factory().create(appApi.get()).also { viewStateApiComponent = it }
        }

        @Provides
        @AppScope
        fun provideEffectHandlers(@BindingExport bindingExports: @JvmSuppressWildcards Set<Any>): Map<Class<out ViewEffect>, EffectHandler<*>> {
            return bindingExports.filterIsInstance<EffectHandlersProvider>()
                .fold(mutableMapOf()) { acc, provider -> acc.apply { putAll(provider.getEffectHandlersMap()) } }
        }

        @Provides
        @AppScope
        fun provideStateReducers(@BindingExport bindingExports: @JvmSuppressWildcards Set<Any>): Map<Class<out ViewIntent>, StateReducer<*>> {
            return bindingExports.filterIsInstance<StateReducersProvider>()
                .fold(mutableMapOf()) { acc, provider -> acc.apply { putAll(provider.getStateReducersMap()) } }
        }

        @Provides
        @AppScope
        fun provideStateVisualizers(@BindingExport bindingExports: @JvmSuppressWildcards Set<Any>): Map<Class<out ViewState>, StateVisualizer<*>> {
            return bindingExports.filterIsInstance<StateVisualizersProvider>()
                .fold(mutableMapOf()) { acc, provider -> acc.apply { putAll(provider.getStateVisualizersMap()) } }
        }
    }
}