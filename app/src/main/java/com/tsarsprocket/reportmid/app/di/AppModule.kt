package com.tsarsprocket.reportmid.app.di

import android.content.Context
import com.tsarsprocket.reportmid.ReportMidApp
import com.tsarsprocket.reportmid.applicationComponent
import com.tsarsprocket.reportmid.base.di.qualifiers.Computation
import com.tsarsprocket.reportmid.base.di.qualifiers.Io
import com.tsarsprocket.reportmid.base.di.qualifiers.Ui
import com.tsarsprocket.reportmid.view_state_api.di.ViewStateCollected
import com.tsarsprocket.reportmid.view_state_api.view_state.EffectHandler
import com.tsarsprocket.reportmid.view_state_api.view_state.StateReducer
import com.tsarsprocket.reportmid.view_state_api.view_state.StateVisualizer
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewEffect
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewIntent
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewState
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
internal class AppModule {

    @Provides
    fun provideAppContext(): Context = ReportMidApp.instance

    @Provides
    @Computation
    fun provideComputationDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Io
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Ui
    fun provideUiDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @ViewStateCollected
    fun provideCollectedEffectHandlers(): Map<Class<out ViewEffect>, @JvmSuppressWildcards EffectHandler<*>> {
        return applicationComponent.getCollectedEffectHandlers()
    }

    @Provides
    @ViewStateCollected
    fun provideCollectedStateReducers(): Map<Class<out ViewIntent>, @JvmSuppressWildcards StateReducer<*>> {
        return applicationComponent.getCollectedStateReducers()
    }

    @Provides
    @ViewStateCollected
    fun provideCollectedStateVisualisers(): Map<Class<out ViewState>, @JvmSuppressWildcards StateVisualizer<*>> {
        return applicationComponent.getCollectedStateVisualizers()
    }
}
