package com.tsarsprocket.reportmid.viewStateImpl.viewmodel

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Aggregated
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Ui
import com.tsarsprocket.reportmid.utils.dagger.findProcessor
import com.tsarsprocket.reportmid.viewStateApi.effectHandler.ViewEffectHandler
import com.tsarsprocket.reportmid.viewStateApi.view.ViewStateFragment
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.ViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateImpl.backstack.BackStack
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Provider

internal class ViewStateViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    @Aggregated private val effectHandlers: Map<Class<out ViewEffect>, @JvmSuppressWildcards Provider<ViewEffectHandler>>,
    @Ui.Immediate val immediateUiDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val mutableViewEffectActions = MutableSharedFlow<suspend (ViewStateFragment) -> Unit>()

    val viewEffectActions: SharedFlow<suspend (ViewStateFragment) -> Unit> = mutableViewEffectActions.asSharedFlow()

    private val holders = mutableMapOf<UUID, ViewStateHolderImpl>()

    val rootHolder: ViewStateHolderImpl = savedStateHandle.get<ViewStateHolderImpl>(KEY_ROOT_HOLDER)!!.apply {
        viewModel = this@ViewStateViewModel
        initializeCoroutineScope(viewModelScope)
        propagateParentHolder()
        start()
    }

    internal val backStack: BackStack = savedStateHandle[KEY_BACKSTACK]!!

    val stackSize: StateFlow<Int>
        get() = backStack.stackSize

    init {
        backStack.holderResolver = holders::get
    }

    fun goBack() {
        backStack.goBack()
    }

    fun postEffect(effect: ViewEffect, holder: ViewStateHolder) {
        viewModelScope.launch(immediateUiDispatcher) {
            mutableViewEffectActions.emit { fragment -> effectHandlers.findProcessor(effect).handle(effect, fragment, holder) }
        }
    }

    fun postIntent(intent: ViewIntent) {
        rootHolder.postIntent(intent)
    }

    fun registerHolder(holder: ViewStateHolderImpl) {
        holders[holder.globalId] = holder
    }

    fun unregisterHolder(holder: ViewStateHolderImpl) {
        holders.remove(holder.globalId)
    }

    fun saveState() {
        println("Saving state....")
        savedStateHandle[KEY_ROOT_HOLDER] = rootHolder
        savedStateHandle[KEY_BACKSTACK] = backStack
        println("State saved")
    }

    @Composable
    fun Visualize() {
        rootHolder.Visualize(Modifier.fillMaxSize())
    }

    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): ViewStateViewModel
    }

    internal companion object {
        const val KEY_BACKSTACK = "backstack"
        const val KEY_ROOT_HOLDER = "root_holder"
    }
}