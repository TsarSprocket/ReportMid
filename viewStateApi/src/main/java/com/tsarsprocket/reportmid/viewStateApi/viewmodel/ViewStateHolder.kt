package com.tsarsprocket.reportmid.viewStateApi.viewmodel

import android.os.Parcelable
import androidx.compose.runtime.Composable
import com.tsarsprocket.reportmid.utils.dagger.findProcessor
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.ViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import javax.inject.Provider

interface ViewStateHolder : Parcelable {
    val coroutineScope: CoroutineScope
    val currentState: ViewState
    val globalId: UUID
    val parentHolder: ViewStateHolder?
    val topReturnIntent: ViewIntent?
    val viewStates: StateFlow<ViewState>
    fun createSubholder(initialState: ViewState): ViewStateHolder
    fun popTopReturnIntent(): ViewIntent
    fun postIntent(intent: ViewIntent, returnIntent: ViewIntent? = null)
    fun postEffect(effect: ViewEffect)
    fun setParentHolder(parentHolder: ViewStateHolder)
    fun setState(state: ViewState)
    fun skipStack(conditionWhile: (ViewIntent) -> Boolean)
    fun start()
    fun stop()
    @Composable
    fun Visualize()
}

fun <IntentMapper> ViewStateHolder.postReturnIntent(processors: Map<Class<out ViewIntent>, Provider<IntentMapper>>, viewIntentProducer: IntentMapper.() -> ViewIntent) {
    postIntent(processors.findProcessor(popTopReturnIntent()).viewIntentProducer())
}
