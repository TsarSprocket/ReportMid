package com.tsarsprocket.reportmid.viewStateApi.viewmodel

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import com.tsarsprocket.reportmid.utils.common.EMPTY_STRING
import com.tsarsprocket.reportmid.utils.dagger.findProcessor
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
    val coroutineScope: CoroutineScope
    val currentState: ViewState
    val globalId: UUID
    val parentHolder: ViewStateHolder?
    val rootHolder: ViewStateHolder
    val tag: String
    val topReturnIntent: ViewIntent?
    val viewStates: StateFlow<ViewState>
    fun createSubholder(tag: String = EMPTY_STRING, initialState: ViewState = EmptyScreenViewState): ViewStateHolder
    fun popTopReturnIntent(): ViewIntent
    fun postIntent(intent: ViewIntent, returnIntent: ViewIntent? = null)
    fun postEffect(effect: ViewEffect)
    fun setParentHolder(parentHolder: ViewStateHolder)
    fun setState(state: ViewState)
    fun skipStack(conditionWhile: (ViewIntent) -> Boolean)
    fun start()
    fun stop()
    @Composable
    fun Visualize(modifier: Modifier)

    companion object {
        const val ROOT_TAG = "root"
    }
}

fun <IntentMapper> ViewStateHolder.postReturnIntent(processors: Map<Class<out ViewIntent>, Provider<IntentMapper>>, viewIntentProducer: IntentMapper.() -> ViewIntent) {
    postIntent(processors.findProcessor(popTopReturnIntent()).viewIntentProducer())
}

fun ViewStateHolder.getTagged(tag: String): ViewStateHolder? = generateSequence(this) { it.parentHolder }.firstOrNull() { it.tag == tag }
