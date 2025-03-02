package com.tsarsprocket.reportmid.viewStateApi.viewState

import androidx.compose.runtime.Composable
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.ViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.QuitViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.EmptyCoroutineContext

object PreviewViewStateHolder : ViewStateHolder {

    override val coroutineScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext)
    override val currentState: ViewState = EmptyScreenViewState
    override val topReturnIntent: ViewIntent? = null
    override val viewStates: StateFlow<ViewState> = MutableStateFlow(currentState)

    override fun createSubholder(initialState: ViewState): ViewStateHolder = this

    override fun popTopReturnIntent(): ViewIntent = QuitViewIntent

    override fun postIntent(intent: ViewIntent, returnIntent: ViewIntent?) { /* nothing */
    }

    override fun postEffect(effect: ViewEffect) { /* nothing */
    }

    override fun skipStack(conditionWhile: (ViewIntent) -> Boolean) { /* nothing */
    }

    @Composable
    override fun Visualize() { /* nothing */
    }
}