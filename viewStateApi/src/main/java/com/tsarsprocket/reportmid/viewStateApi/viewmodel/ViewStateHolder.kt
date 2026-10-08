package com.tsarsprocket.reportmid.viewStateApi.viewmodel

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import com.tsarsprocket.reportmid.utils.common.EMPTY_STRING
import com.tsarsprocket.reportmid.viewStateApi.backstack.BackStack
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.ViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.EmptyScreenViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import javax.inject.Provider

@Stable
interface ViewStateHolder : Parcelable {
    val viewHolderScope: CoroutineScope
    val currentState: ViewState
    val stateCoroutineScope: CoroutineScope
    val globalId: UUID
    val parentHolder: ViewStateHolder?
    val rootHolder: ViewStateHolder
    val tag: String
    val topReturnIntent: ViewIntent?
    val viewStates: StateFlow<ViewState>
    val backStack: BackStack
    val lastOperationUuid: UUID
    fun createSubholder(tag: String = EMPTY_STRING, initialState: ViewState = EmptyScreenViewState): ViewStateHolder
    fun getTagged(tag: String): ViewStateHolder?
    fun popTopReturnIntent(): ViewIntent
    fun postIntent(intent: ViewIntent, returnIntent: ViewIntent? = null)
    fun postEffect(effect: ViewEffect)
    fun <IntentMapper> postReturnIntent(processors: Map<Class<out ViewIntent>, Provider<IntentMapper>>, viewIntentProducer: IntentMapper.() -> ViewIntent)
    fun setParentHolder(parentHolder: ViewStateHolder)
    fun start()
    fun stop()
    @Composable
    fun Visualize(modifier: Modifier)
    fun pushReturnIntent(viewIntent: ViewIntent)

    companion object {
        const val ROOT_TAG = "root"
    }
}
