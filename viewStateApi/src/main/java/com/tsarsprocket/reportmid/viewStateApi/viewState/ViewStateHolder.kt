package com.tsarsprocket.reportmid.viewStateApi.viewState

import androidx.compose.runtime.Composable
import com.tsarsprocket.reportmid.utils.dagger.findProcessor
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.ViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Provider

interface ViewStateHolder {
    val coroutineScope: CoroutineScope
    val currentState: ViewState
    val topReturnIntent: ViewIntent?
    val viewStates: StateFlow<ViewState>
    fun createSubholder(initialState: ViewState): ViewStateHolder
    fun popTopReturnIntent(): ViewIntent
    fun postIntent(intent: ViewIntent, returnIntent: ViewIntent? = null)
    fun postEffect(effect: ViewEffect)
    fun skipStack(conditionWhile: (ViewIntent) -> Boolean)
    @Composable
    fun Visualize()
}

fun <IntentMapper> ViewStateHolder.postReturnIntent(processors: Map<Class<out ViewIntent>, Provider<IntentMapper>>, viewIntentProducer: IntentMapper.() -> ViewIntent) {
    postIntent(processors.findProcessor(popTopReturnIntent()).viewIntentProducer())
}
