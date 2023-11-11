package com.tsarsprocket.reportmid.view_state_impl.view_model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.Fragment
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
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewStateHolder
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
) : ViewModel() {

    private val mutableViewEffects = MutableSharedFlow<suspend (Fragment) -> Unit>()
    val viewEffects = mutableViewEffects.asSharedFlow()

    private val theHolder = Holder(GeneralViewStateCluster.Initial)
    val rootHolder: ViewStateHolder = theHolder

    private val backStack = BackStack()
    val stackSize: StateFlow<Int>
        get() = backStack.stackSize

    fun tryGoBack(): Boolean {
        return backStack.pop()?.let { (holder, state) ->
            (holder as Holder).setState(state)
            true
        } ?: false
    }

    fun setInitialState(state: ViewState) {
        theHolder.setState(state)
    }

    private fun findEffectHandler(effect: ViewEffect): EffectHandler<ViewEffect>? {
        @Suppress("UNCHECKED_CAST")
        return (effectHandlers[effect.clusterClass.java] as? EffectHandler<ViewEffect>)
    }

    private fun findStateVisualizer(state: ViewState): StateVisualizer<ViewState>? {
        @Suppress("UNCHECKED_CAST")
        return (stateVisualizers[state.clusterClass.java] as? StateVisualizer<ViewState>)
    }

    private fun postEffect(effect: ViewEffect, holder: ViewStateHolder) {
        findEffectHandler(effect)?.let { handler ->
            viewModelScope.launch(uiDispatcher) {
                mutableViewEffects.emit { fragment -> handler.handleEffect(effect, fragment, holder) }
            }
        }
    }

    private inner class Holder(initialState: ViewState) : ViewStateHolder {

        private val mutableViewStates = MutableStateFlow(initialState)
        private val mutableViewIntents = MutableSharedFlow<Pair<ViewIntent, ViewState?>>()
        override val viewStates = mutableViewStates.asStateFlow()

        init {
            viewModelScope.launch {
                mutableViewIntents.collect { (intent, goBackState) ->
                    processIntent(intent, goBackState)
                }
            }
        }

        override fun createSubholder(initialState: ViewState) = Holder(initialState)

        override fun postIntent(intent: ViewIntent, goBackState: ViewState?) {
            viewModelScope.launch(uiDispatcher) {
                mutableViewIntents.emit(intent to goBackState)
            }
        }

        override fun postEffect(effect: ViewEffect) {
            this@ViewStateViewModel.postEffect(effect, this)
        }

        @Composable
        override fun Visualize() {
            val state = mutableViewStates.collectAsState().value
            findStateVisualizer(state)?.Visualize(state, this)
        }

        fun setState(state: ViewState) {
            mutableViewStates.value = state
        }

        private suspend fun processIntent(intent: ViewIntent, goBackState: ViewState?) {
            @Suppress("UNCHECKED_CAST")
            (stateReducers[intent.clusterClass.java] as? StateReducer<ViewIntent>)?.reduce(viewStates.value, intent, this)?.let { viewState ->
                goBackState?.let { backStack.push(BackStack.Entry(this, it)) }
                mutableViewStates.emit(viewState)
            }
        }
    }
}