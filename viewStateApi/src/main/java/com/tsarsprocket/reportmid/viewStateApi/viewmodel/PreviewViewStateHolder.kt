package com.tsarsprocket.reportmid.viewStateApi.viewmodel

import android.annotation.SuppressLint
import android.os.Parcel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tsarsprocket.reportmid.utils.common.EMPTY_STRING
import com.tsarsprocket.reportmid.viewStateApi.backstack.BackStack
import com.tsarsprocket.reportmid.viewStateApi.backstack.BackStackEntry
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.ViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.QuitViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.EmptyScreenViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import javax.inject.Provider
import kotlin.coroutines.EmptyCoroutineContext

@SuppressLint("ParcelCreator") // Never supposed to be parcelled
object PreviewViewStateHolder : ViewStateHolder {

    override val viewHolderScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext)
    override val currentState: ViewState = EmptyScreenViewState
    override val stateCoroutineScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext)
    override val globalId: UUID = UUID.randomUUID()
    override val parentHolder: ViewStateHolder? = null
    override val rootHolder: ViewStateHolder = this
    override val tag: String = EMPTY_STRING
    override val topReturnIntent: ViewIntent? = null
    override val viewStates: StateFlow<ViewState> = MutableStateFlow(currentState)
    override val backStack: BackStack = object : BackStack {
        override val entriesFlow: StateFlow<List<BackStackEntry>> = MutableStateFlow(emptyList())
        override fun goBack() {}
        override fun goBackTo(uuid: UUID) {}
        override fun push(holder: ViewStateHolder, operationUuid: UUID) {}
        override fun describeContents() = 0
        override fun writeToParcel(dest: Parcel, flags: Int) {}
    }
    override val lastOperationUuid: UUID = UUID.randomUUID()
    override fun createSubholder(tag: String, initialState: ViewState): ViewStateHolder = this
    override fun getTagged(tag: String): ViewStateHolder? = null

    override fun describeContents() = 0
    override fun postIntent(intent: ViewIntent, returnIntent: ViewIntent?) {}
    override fun popTopReturnIntent(): ViewIntent = QuitViewIntent
    override fun postEffect(effect: ViewEffect) {}
    override fun <IntentMapper> postReturnIntent(processors: Map<Class<out ViewIntent>, Provider<IntentMapper>>, viewIntentProducer: IntentMapper.() -> ViewIntent) {}

    override fun setParentHolder(parentHolder: ViewStateHolder) {}
    override fun start() {}
    override fun stop() {}

    @Composable
    override fun Visualize(modifier: Modifier) {
    }

    override fun pushReturnIntent(viewIntent: ViewIntent) {}
    override fun writeToParcel(p0: Parcel, p1: Int) {}
}