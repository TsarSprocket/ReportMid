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
import com.tsarsprocket.reportmid.viewStateApi.backstack.BackStack
import com.tsarsprocket.reportmid.viewStateApi.effectHandler.ViewEffectHandler
import com.tsarsprocket.reportmid.viewStateApi.view.ViewStateFragment
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.ViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
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
    @param:Aggregated private val effectHandlers: Map<Class<out ViewEffect>, @JvmSuppressWildcards Provider<ViewEffectHandler>>,
    @param:Ui.Immediate val immediateUiDispatcher: CoroutineDispatcher,
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

    /**
     * Restores the state identified by the back operation [uuid], unwinding every intermediate back operation
     * along the way.
     *
     * Starting from the top of the back stack, operations are inspected one by one until the one identified by
     * [uuid] is reached (the "target" operation, and the holder it belongs to the "target" holder). For each
     * intermediate operation:
     * - If the holder it belongs to is part of the target holder's hierarchy (i.e. the target holder can be
     *   reached by following [ViewStateHolderImpl.parentHolder] references from it), the operation is discarded
     *   without invoking its back [ViewIntent].
     * - Otherwise, the operation is left untouched and the walk continues to the next one down the stack.
     *
     * Once the target operation is reached, it is removed and its back [ViewIntent] is invoked.
     *
     * The whole back stack manipulation happens synchronously; only the final back-intent dispatch may involve
     * asynchronous work, and it only happens after every other adjustment has completed.
     */
    fun goBackTo(uuid: UUID) {
        val operationIds = backStack.operationIdsFromTopTo(uuid)
        val targetHolderId = operationIds.lastOrNull()?.let(backStack::holderIdFor) ?: return
        val targetHolder = holders[targetHolderId] ?: return

        for(operationId in operationIds) {
            val holder = backStack.holderIdFor(operationId)?.let(holders::get) ?: continue

            if(operationId == uuid) {
                holder.doGoBack(operationId)
            } else if(isWithinHierarchy(holder, targetHolder)) {
                holder.removeOperation(operationId)
            }
        }
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

    /**
     * Returns `true` if [target] is reachable from [holder] by following [ViewStateHolderImpl.parentHolder]
     * references (including when [holder] *is* [target]), `false` if the root is reached first.
     */
    private fun isWithinHierarchy(holder: ViewStateHolderImpl, target: ViewStateHolderImpl): Boolean {
        var current: ViewStateHolderImpl? = holder
        while(current != null) {
            if(current === target) return true
            current = current.parentHolder
        }
        return false
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