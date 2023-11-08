package com.tsarsprocket.reportmid.view_state_impl.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsarsprocket.reportmid.base.di.qualifiers.Ui
import com.tsarsprocket.reportmid.view_state_api.di.ViewStateCollected
import com.tsarsprocket.reportmid.view_state_api.view_state.EffectHandler
import com.tsarsprocket.reportmid.view_state_api.view_state.GeneralViewStateCluster
import com.tsarsprocket.reportmid.view_state_api.view_state.StateReducer
import com.tsarsprocket.reportmid.view_state_api.view_state.StateVisualizer
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewEffect
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewIntent
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewState
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewStateController
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class ViewStateViewModel @Inject constructor(
    @ViewStateCollected private val effectHandlers: Map<Class<out ViewEffect>, @JvmSuppressWildcards EffectHandler<*>>,
    @ViewStateCollected private val stateReducers: Map<Class<out ViewIntent>, @JvmSuppressWildcards StateReducer<*>>,
    @ViewStateCollected private val stateVisualizers: Map<Class<out ViewState>, @JvmSuppressWildcards StateVisualizer<*>>,
    @Ui private val uiDispatcher: CoroutineDispatcher,
) : ViewStateController, ViewModel() {

    private val mutableViewStates = MutableStateFlow<ViewState>(GeneralViewStateCluster.Initial)
    private val mutableViewEffects = MutableSharedFlow<ViewEffect>()
    private val mutableViewIntents = MutableSharedFlow<Pair<ViewIntent, ViewState?>>()

    val viewStates = mutableViewStates.asStateFlow()
    val viewEffects = mutableViewEffects.asSharedFlow()

    private val backStack = BackStack()
    val stackSize: StateFlow<Int>
        get() = backStack.stackSize

    init {
        viewModelScope.launch {
            mutableViewIntents.collect { (intent, goBackState) ->
                processIntent(intent, goBackState)
            }
        }
    }

    fun tryGoBack(): Boolean {
        return backStack.pop()?.let {
            mutableViewStates.value = it
            true
        } ?: false
    }

    override fun postEffect(effect: ViewEffect) {
        viewModelScope.launch(uiDispatcher) {
            mutableViewEffects.emit(effect)
        }
    }

    override fun postIntent(intent: ViewIntent, goBackState: ViewState?) {
        viewModelScope.launch(uiDispatcher) {
            mutableViewIntents.emit(intent to goBackState)
        }
    }

    fun findEffectHandler(effect: ViewEffect): EffectHandler<ViewEffect>? {
        @Suppress("UNCHECKED_CAST")
        return (effectHandlers[effect.clusterClass.java] as? EffectHandler<ViewEffect>)
    }

    fun findStateVisualizer(state: ViewState): StateVisualizer<ViewState>? {
        @Suppress("UNCHECKED_CAST")
        return (stateVisualizers[state.clusterClass.java] as? StateVisualizer<ViewState>)
    }

    private suspend fun processIntent(intent: ViewIntent, goBackState: ViewState?) {
        @Suppress("UNCHECKED_CAST")
        (stateReducers[intent.clusterClass.java] as? StateReducer<ViewIntent>)?.reduce(viewStates.value, intent, this)?.let { viewState ->
            goBackState?.let { backStack.push(it) }
            mutableViewStates.emit(viewState)
        }
    }
}