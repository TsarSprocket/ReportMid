package com.tsarsprocket.reportmid.view_state_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base.di.Api
import com.tsarsprocket.reportmid.base.di.ApiKey
import com.tsarsprocket.reportmid.base.di.AppScope
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
import dagger.multibindings.IntoMap
import javax.inject.Provider

@Module
interface ViewStateProvisionModule {

    @Binds
    @IntoMap
    @ApiKey(ViewStateApi::class)
    fun bindToApi(api: ViewStateApi): Api

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
        fun provideEffectHandlers(apiMap: Map<Class<out Api>, @JvmSuppressWildcards Provider<Api>>): Map<Class<out ViewEffect>, EffectHandler<*>> {
            return apiMap.entries.filter { (cls, _) -> EffectHandlersProvider::class.java.isAssignableFrom(cls) }
                .map { (_, provider) -> provider.get() }
                .fold(mutableMapOf()) { acc, api ->
                    acc.apply { putAll((api as EffectHandlersProvider).getEffectHandlersMap()) }
                }
        }

        @Provides
        @AppScope
        fun provideStateReducers(apiMap: Map<Class<out Api>, @JvmSuppressWildcards Provider<Api>>): Map<Class<out ViewIntent>, StateReducer<*>> {
            return apiMap.entries.filter { (cls, _) -> StateReducersProvider::class.java.isAssignableFrom(cls) }
                .map { (_, provider) -> provider.get() }
                .fold(mutableMapOf()) { acc, api ->
                    acc.apply { putAll((api as StateReducersProvider).getStateReducersMap()) }
                }
        }

        @Provides
        @AppScope
        fun provideStateVisualizers(apiMap: Map<Class<out Api>, @JvmSuppressWildcards Provider<Api>>): Map<Class<out ViewState>, StateVisualizer<*>> {
            return apiMap.entries.filter { (cls, _) -> StateVisualizersProvider::class.java.isAssignableFrom(cls) }
                .map { (_, provider) -> provider.get() }
                .fold(mutableMapOf()) { acc, api ->
                    acc.apply { putAll((api as StateVisualizersProvider).getStateVisualizersMap()) }
                }
        }
    }
}