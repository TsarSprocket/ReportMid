package com.tsarsprocket.reportmid.viewStateImpl.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Ui
import com.tsarsprocket.reportmid.viewStateApi.view_state.EmptyScreen
import com.tsarsprocket.reportmid.viewStateApi.view_state.ViewEffect
import com.tsarsprocket.reportmid.viewStateApi.view_state.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.view_state.ViewState
import com.tsarsprocket.reportmid.viewStateApi.view_state.ViewStateHolder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class ViewStateViewModel @Inject constructor(
    @Ui private val uiDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val mutableViewEffects = MutableSharedFlow<suspend (Fragment) -> Unit>()
    val viewEffects = mutableViewEffects.asSharedFlow()

    private val theHolder = Holder(EmptyScreen)
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

    private fun postEffect(effect: ViewEffect, holder: ViewStateHolder) {
        viewModelScope.launch(uiDispatcher) {
            mutableViewEffects.emit { fragment -> effect.handle(fragment, holder) }
        }
    }

    private inner class Holder(initialState: ViewState) : ViewStateHolder {

        override val coroutineScope: CoroutineScope
            get() = viewModelScope

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
            mutableViewStates.collectAsState().value.Visualize(this)
        }

        fun setState(state: ViewState) {
            mutableViewStates.value = state
        }

        private suspend fun processIntent(intent: ViewIntent, goBackState: ViewState?) {
            val viewState = intent.reduce(viewStates.value, this)
            goBackState?.let { backStack.push(BackStack.Entry(this, it)) }
            mutableViewStates.emit(viewState)
        }
    }
}