package com.tsarsprocket.reportmid.appApi.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.appApi.room.MainStorage
import com.tsarsprocket.reportmid.baseApi.di.AppContext
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Aggregated
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Computation
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Io
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Ui
import com.tsarsprocket.reportmid.baseApi.viewmodel.ViewModelFactoryCreator
import com.tsarsprocket.reportmid.viewStateApi.effectHandler.ViewEffectHandler
import com.tsarsprocket.reportmid.viewStateApi.reducer.ViewStateReducer
import com.tsarsprocket.reportmid.viewStateApi.stateInitializer.ViewStateInitializer
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.ViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.visualizer.ViewStateVisualizer
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Provider

interface AppApi {

    @AppContext
    fun getAppContext(): Context

    @Ui
    fun getUiDispatcher(): CoroutineDispatcher

    @Ui.Immediate
    fun getImmediateUiDispatcher(): CoroutineDispatcher

    @Io
    fun getIoDispatcher(): CoroutineDispatcher

    @Computation
    fun getComputationDispatcher(): CoroutineDispatcher

    fun getMainStorage(): MainStorage

    @Aggregated
    fun getFragmentCreators(): Map<Class<out Fragment>, Provider<Fragment>>

    @Aggregated
    fun getViewModelFactoryCreators(): Map<Class<out ViewModel>, Provider<ViewModelFactoryCreator>>

    @Aggregated
    fun getViewStateInitializers(): Map<Class<out ViewState>, Provider<ViewStateInitializer>>

    @Aggregated
    fun getViewStateReducers(): Map<Class<out ViewIntent>, Provider<ViewStateReducer>>

    @Aggregated
    fun getVisualizers(): Map<Class<out ViewState>, Provider<ViewStateVisualizer>>

    @Aggregated
    fun getEffectHandlers(): Map<Class<out ViewEffect>, Provider<ViewEffectHandler>>
}