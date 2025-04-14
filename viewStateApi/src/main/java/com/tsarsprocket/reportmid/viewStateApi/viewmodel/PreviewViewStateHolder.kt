package com.tsarsprocket.reportmid.viewStateApi.viewmodel

import android.annotation.SuppressLint
import android.os.Parcel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tsarsprocket.reportmid.utils.common.EMPTY_STRING
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.ViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.QuitViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.EmptyScreenViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import kotlin.coroutines.EmptyCoroutineContext

@SuppressLint("ParcelCreator") // Never supposed to be parcelled
object PreviewViewStateHolder : ViewStateHolder {

    override val coroutineScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext)
    override val currentState: ViewState = EmptyScreenViewState
    override val globalId: UUID = UUID.randomUUID()
    override val parentHolder: ViewStateHolder? = null
    override val rootHolder: ViewStateHolder = this
    override val tag: String = EMPTY_STRING
    override val topReturnIntent: ViewIntent? = null
    override val viewStates: StateFlow<ViewState> = MutableStateFlow(currentState)

    override fun createSubholder(tag: String, initialState: ViewState): ViewStateHolder = this

    override fun describeContents() = 0

    override fun postIntent(intent: ViewIntent, returnIntent: ViewIntent?) { /* nothing */
    }

    override fun popTopReturnIntent(): ViewIntent = QuitViewIntent

    override fun postEffect(effect: ViewEffect) { /* nothing */
    }

    override fun setParentHolder(parentHolder: ViewStateHolder) { /* */
    }

    override fun setState(state: ViewState) { /* nothing */
    }

    override fun skipStack(conditionWhile: (ViewIntent) -> Boolean) { /* nothing */
    }

    override fun start() { /* nothing */
    }

    override fun stop() { /* nothing */
    }

    @Composable
    override fun Visualize(modifier: Modifier) { /* nothing */
    }

    override fun writeToParcel(p0: Parcel, p1: Int) { /* Never to be called */
    }
}