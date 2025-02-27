package com.tsarsprocket.reportmid.viewStateImpl.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Aggregated
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Ui
import com.tsarsprocket.reportmid.utils.dagger.findProcessor
import com.tsarsprocket.reportmid.viewStateApi.effectHandler.ViewEffectHandler
import com.tsarsprocket.reportmid.viewStateApi.reducer.ViewStateReducer
import com.tsarsprocket.reportmid.viewStateApi.view.ViewStateFragment
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.ViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.EmptyScreenViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.visualizer.ViewStateVisualizer
import com.tsarsprocket.reportmid.viewStateImpl.backstack.BackOperation
import com.tsarsprocket.reportmid.viewStateImpl.backstack.BackStack
import com.tsarsprocket.reportmid.viewStateImpl.viewState.InternalViewStateHolder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

internal class ViewStateViewModel @Inject constructor(
    @Ui private val uiDispatcher: CoroutineDispatcher,
    @Aggregated private val reducers: Map<Class<out ViewIntent>, @JvmSuppressWildcards Provider<ViewStateReducer>>,
    @Aggregated private val visualizers: Map<Class<out ViewState>, @JvmSuppressWildcards Provider<ViewStateVisualizer>>,
    @Aggregated private val effectHandlers: Map<Class<out ViewEffect>, @JvmSuppressWildcards Provider<ViewEffectHandler>>,
) : ViewModel() {

    private val mutableViewEffectActions = MutableSharedFlow<suspend (ViewStateFragment) -> Unit>()
    val viewEffectActions = mutableViewEffectActions.asSharedFlow()

    private val theHolder = Holder(EmptyScreenViewState)
    val rootHolder: ViewStateHolder = theHolder

    private val backStack = BackStack()
    val stackSize: StateFlow<Int>
        get() = backStack.stackSize

    fun goBack() {
        backStack.goBack()
    }

    fun setInitialState(state: ViewState) {
        theHolder.setState(state)
    }

    private fun postEffect(effect: ViewEffect, holder: ViewStateHolder) {
        viewModelScope.launch(uiDispatcher) {
            mutableViewEffectActions.emit { fragment -> effectHandlers.findProcessor(effect).handle(effect, fragment, holder) }
        }
    }

    private inner class Holder(initialState: ViewState) : InternalViewStateHolder {

        override val coroutineScope: CoroutineScope
            get() = viewModelScope

        private val mutableViewStates = MutableStateFlow(initialState)
        private val mutableViewIntents = MutableSharedFlow<ViewIntent>()

        override val viewStates = mutableViewStates.asStateFlow()
        override val currentState: ViewState
            get() = mutableViewStates.value

        private val operationsStack = mutableListOf<BackOperation>()
        override val topReturnIntent: ViewIntent?
            get() = operationsStack.lastOrNull()?.goBackIntent

        init {
            viewModelScope.launch {
                mutableViewIntents.collect { intent ->
                    processIntent(intent)
                }
            }
        }

        override fun createSubholder(initialState: ViewState) = Holder(initialState)

        override fun doGoBack() {
            val operation = operationsStack.removeAt(operationsStack.lastIndex)
            backStack.removeOperation(operation.uuid)
            postIntent(operation.goBackIntent)
        }

        override fun popTopReturnIntent(): ViewIntent = operationsStack.removeAt(operationsStack.lastIndex).goBackIntent

        override fun postEffect(effect: ViewEffect) {
            this@ViewStateViewModel.postEffect(effect, this)
        }

        override fun postIntent(intent: ViewIntent, returnIntent: ViewIntent?) {
            returnIntent?.let { pushReturnIntent(returnIntent) }
            viewModelScope.launch(uiDispatcher) {
                mutableViewIntents.emit(intent)
            }
        }

        override fun skipStack(conditionWhile: (ViewIntent) -> Boolean) {
            while(operationsStack.getOrNull(operationsStack.lastIndex)?.let { conditionWhile(it.goBackIntent) } == true) operationsStack.removeAt(operationsStack.lastIndex)
        }

        @Composable
        override fun Visualize() {
            mutableViewStates.collectAsState().value.let { state -> visualizers.findProcessor(state).Visualize(state, this) }
        }

        fun setState(state: ViewState) {
            mutableViewStates.value = state
        }

        private suspend fun processIntent(intent: ViewIntent) {
            mutableViewStates.emit(reducers.findProcessor(intent).reduce(intent, viewStates.value, this))
        }

        private fun pushReturnIntent(viewIntent: ViewIntent) {
            val operation = BackOperation(viewIntent)
            operationsStack.add(operation)
            backStack.push(this, operation.uuid)
        }
    }
}