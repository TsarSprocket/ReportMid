package com.tsarsprocket.reportmid.viewStateImpl.viewmodel

import android.os.Parcel
import android.os.ParcelUuid
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewModelScope
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Aggregated
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Ui
import com.tsarsprocket.reportmid.utils.common.logInfo
import com.tsarsprocket.reportmid.utils.dagger.findProcessor
import com.tsarsprocket.reportmid.viewStateApi.reducer.ViewStateReducer
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.ViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.visualizer.ViewStateVisualizer
import com.tsarsprocket.reportmid.viewStateImpl.backstack.BackOperation
import com.tsarsprocket.reportmid.viewStateImpl.di.component
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Provider

internal class ViewStateHolderImpl private constructor(
    override val globalId: UUID,
    initialState: ViewState,
    private val operationsStack: MutableList<BackOperation>,
) : ViewStateHolder {

    @Inject
    @Aggregated
    lateinit var reducers: Map<Class<out ViewIntent>, @JvmSuppressWildcards Provider<ViewStateReducer>>

    @Inject
    @Ui.Immediate
    lateinit var immediateUiDispatcher: CoroutineDispatcher

    @Inject
    @Aggregated
    lateinit var visualizers: Map<Class<out ViewState>, @JvmSuppressWildcards Provider<ViewStateVisualizer>>

    override val coroutineScope: CoroutineScope
        get() = viewModel.viewModelScope


    lateinit var viewModel: ViewStateViewModel
    override var parentHolder: ViewStateHolderImpl? = null

    private var intentJob: Job? = null

    private val mutableViewStates = MutableStateFlow(initialState)
    private val mutableViewIntents = MutableSharedFlow<ViewIntent>()

    override val viewStates = mutableViewStates.asStateFlow()
    override val currentState: ViewState
        get() = mutableViewStates.value

    override val topReturnIntent: ViewIntent?
        get() = operationsStack.lastOrNull()?.goBackIntent

    init {
        component.inject(this)
    }

    constructor(initialViewState: ViewState) : this(
        globalId = UUID.randomUUID(),
        initialState = initialViewState,
        operationsStack = mutableListOf<BackOperation>(),
    )

    private constructor(parcel: Parcel) : this(
        globalId = parcel.readParcelable<ParcelUuid>(ViewStateHolderImpl::class.java.classLoader)!!.uuid,
        initialState = parcel.readParcelable(ViewStateHolderImpl::class.java.classLoader)!!,
        operationsStack = parcel.readParcelableArray(ViewStateHolderImpl::class.java.classLoader)
            ?.filterIsInstance<BackOperation>()
            .orEmpty()
            .toMutableList(),
    )

    override fun createSubholder(initialState: ViewState) = ViewStateHolderImpl(initialState).apply { setParentHolder(this@ViewStateHolderImpl) }

    override fun describeContents() = 0

    fun doGoBack() {
        val operation = operationsStack.removeAt(operationsStack.lastIndex)
        viewModel.backStack.removeOperation(operation.uuid)
        postIntent(operation.goBackIntent)
    }

    override fun popTopReturnIntent(): ViewIntent = operationsStack.removeAt(operationsStack.lastIndex).goBackIntent

    override fun postEffect(effect: ViewEffect) {
        viewModel.postEffect(effect, this)
    }

    override fun postIntent(intent: ViewIntent, returnIntent: ViewIntent?) {
        logInfo("ViewIntent posted: $intent")
        returnIntent?.let { pushReturnIntent(returnIntent) }
        coroutineScope.launch(immediateUiDispatcher) {
            mutableViewIntents.emit(intent)
        }
    }

    fun propagateParentHolder() {
        currentState.setParentHolder(this)
    }

    override fun setParentHolder(parentHolder: ViewStateHolder) {
        this.parentHolder = parentHolder as ViewStateHolderImpl
        this.viewModel = parentHolder.viewModel
        propagateParentHolder()
    }

    override fun setState(state: ViewState) {
        mutableViewStates.value.stop()
        state.setParentHolder(this)
        mutableViewStates.value = state
        state.start()
    }

    override fun skipStack(conditionWhile: (ViewIntent) -> Boolean) {
        while(operationsStack.getOrNull(operationsStack.lastIndex)?.let { conditionWhile(it.goBackIntent) } == true) operationsStack.removeAt(operationsStack.lastIndex)
    }

    override fun start() {
        viewModel.registerHolder(this)
        intentJob?.cancel()
        intentJob = coroutineScope.launch {
            mutableViewIntents.collect { intent ->
                processIntent(intent)
            }
        }
        currentState.start()
    }

    override fun stop() {
        intentJob?.cancel()
        intentJob = null
        viewModel.unregisterHolder(this)
    }

    @Composable
    override fun Visualize() {
        mutableViewStates.collectAsState().value.let { state -> visualizers.findProcessor(state).Visualize(state, this) }
    }

    private suspend fun processIntent(intent: ViewIntent) {
        val oldState = viewStates.value

        val newState = reducers.findProcessor(intent)
            .reduce(
                intent = intent,
                state = oldState,
                stateHolder = this
            )

        if(newState !== oldState) {
            oldState.stop()
            newState.setParentHolder(this@ViewStateHolderImpl)
            newState.start()
            mutableViewStates.emit(newState)
            logInfo("View state emitted: $newState")
        }
    }

    private fun pushReturnIntent(viewIntent: ViewIntent) {
        val operation = BackOperation(viewIntent)
        operationsStack.add(operation)
        viewModel.backStack.push(this, operation.uuid)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        with(parcel) {
            writeParcelable(ParcelUuid(globalId), flags)
            writeParcelable(mutableViewStates.value, flags)
            writeParcelableArray(operationsStack.toTypedArray(), flags)
        }
    }

    companion object CREATOR : Parcelable.Creator<ViewStateHolderImpl?> {
        override fun createFromParcel(parcel: Parcel?): ViewStateHolderImpl? = parcel?.let { ViewStateHolderImpl(it) }
        override fun newArray(size: Int): Array<ViewStateHolderImpl?> = arrayOfNulls(size)
    }
}