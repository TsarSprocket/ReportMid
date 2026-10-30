package com.tsarsprocket.reportmid.viewStateImpl.viewmodel

import android.os.Parcel
import android.os.ParcelUuid
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Aggregated
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Ui
import com.tsarsprocket.reportmid.utils.common.logError
import com.tsarsprocket.reportmid.utils.common.logInfo
import com.tsarsprocket.reportmid.utils.coroutines.SupervisorChildCoroutineScope
import com.tsarsprocket.reportmid.utils.dagger.findProcessor
import com.tsarsprocket.reportmid.utils.dagger.findProcessorOrNull
import com.tsarsprocket.reportmid.viewStateApi.backstack.BackStack
import com.tsarsprocket.reportmid.viewStateApi.backstack.BackStackEntry
import com.tsarsprocket.reportmid.viewStateApi.reducer.ViewStateReducer
import com.tsarsprocket.reportmid.viewStateApi.stateInitializer.ViewStateInitializer
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.ViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.visualizer.ViewStateVisualizer
import com.tsarsprocket.reportmid.viewStateImpl.di.component
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Provider

internal class ViewStateHolderImpl private constructor(
    override val globalId: UUID,
    private val initialState: ViewState,
    private val operationsStack: MutableList<BackStackEntry>,
    override val tag: String,
) : InternalViewStateHolder {

    @Inject
    @Aggregated
    lateinit var reducers: Map<Class<out ViewIntent>, @JvmSuppressWildcards Provider<ViewStateReducer>>

    @Inject
    @Aggregated
    lateinit var initializers: Map<Class<out ViewState>, @JvmSuppressWildcards Provider<ViewStateInitializer>>

    @Inject
    @Aggregated
    lateinit var visualizers: Map<Class<out ViewState>, @JvmSuppressWildcards Provider<ViewStateVisualizer>>

    @Inject
    @Ui.Immediate
    lateinit var immediateUiDispatcher: CoroutineDispatcher

    override lateinit var viewHolderScope: CoroutineScope

    override lateinit var viewModel: ViewStateViewModel
    override var parentHolder: InternalViewStateHolder? = null
    override val rootHolder: InternalViewStateHolder
        get() = viewModel.rootHolder

    private var intentJob: Job? = null

    private val mutableViewStates = MutableStateFlow(initialState)
    private val mutableViewIntents = MutableSharedFlow<ViewIntent>()

    override val viewStates = mutableViewStates.asStateFlow()
    override val currentState: ViewState
        get() = mutableViewStates.value

    private var _stateCoroutineScope: CoroutineScope? = null

    // Use stateCoroutineScope for state-scoped operations
    override val stateCoroutineScope: CoroutineScope
        get() = _stateCoroutineScope ?: SupervisorChildCoroutineScope(viewHolderScope).also { _stateCoroutineScope = it }

    override val topReturnIntent: ViewIntent?
        get() = operationsStack.lastOrNull()?.viewIntent

    override val backStack: BackStack
        get() = viewModel.backStack

    override val lastOperationUuid: UUID
        get() = operationsStack.last().uuid

    init {
        component.inject(this)
    }

    constructor(tag: String, initialViewState: ViewState) : this(
        globalId = UUID.randomUUID(),
        initialState = initialViewState,
        operationsStack = mutableListOf(),
        tag = tag,
    )

    private constructor(parcel: Parcel) : this(
        globalId = parcel.readParcelable<ParcelUuid>(ViewStateHolderImpl::class.java.classLoader)!!.uuid,
        initialState = parcel.readParcelable(ViewStateHolderImpl::class.java.classLoader)!!,
        operationsStack = parcel.readParcelableArray(ViewStateHolderImpl::class.java.classLoader)
            ?.filterIsInstance<BackStackEntry>()
            .orEmpty()
            .toMutableList(),
        tag = parcel.readString().orEmpty()
    )

    override fun createSubholder(tag: String, initialState: ViewState): ViewStateHolderImpl {
        return ViewStateHolderImpl(tag, initialState).apply {
            setParentHolder(this@ViewStateHolderImpl)
        }
    }

    override fun findTaggedParent(tag: String): ViewStateHolder? = generateSequence<InternalViewStateHolder>(this) { it.parentHolder }.firstOrNull() { it.tag == tag }

    override fun describeContents() = 0

    override fun doGoBack(uuid: UUID) {
        backStack.remove(uuid)?.let { backStackEntry ->
            postIntent(backStackEntry.viewIntent)
        }
    }

    /**
     * Removes the operation identified by [uuid] from this holder's own local stack only, without notifying
     * the global back stack. A no-op if [uuid] is not present on this holder's stack.
     */
    override fun extractOperation(uuid: UUID): BackStackEntry? {
        val iterator = operationsStack.iterator()

        while(iterator.hasNext()) {
            val entry = iterator.next()
            if(entry.uuid == uuid) {
                iterator.remove()
                return entry
            }
        }

        return null
    }

    /**
     * Looks up the [ViewIntent] of the operation identified by [uuid] on this holder's local stack, without
     * removing it. Used by the global back stack to expose [BackStack.entriesFlow] without keeping its own
     * copy of each operation's [ViewIntent].
     */
    override fun goBackOperationFor(uuid: UUID): BackStackEntry? = operationsStack.find { it.uuid == uuid }

    override fun initializeCoroutineScope(scope: CoroutineScope) {
        viewHolderScope = SupervisorChildCoroutineScope(scope)
    }

    override fun popTopReturnIntent(): ViewIntent = backStack.remove(operationsStack.last().uuid)!!.viewIntent

    override fun postEffect(effect: ViewEffect) {
        viewModel.postEffect(effect, this)
    }

    override fun postIntent(intent: ViewIntent, returnIntent: ViewIntent?, routeTo: UUID) {
        logInfo("ViewIntent posted: $intent${if(returnIntent != null) " with return intent: $returnIntent" else ""}${if(routeTo != globalId) " to $routeTo" else ""}")
        if(routeTo == globalId) {
            postIntentToSelf(intent, returnIntent)
        } else {
            viewModel.findHolder(routeTo)?.postIntentToSelf(intent, returnIntent)
        }
    }

    override fun postIntentToSelf(intent: ViewIntent, returnIntent: ViewIntent?) {
        returnIntent?.let { pushReturnIntent(returnIntent) }
        viewHolderScope.launch(immediateUiDispatcher) {
            mutableViewIntents.emit(intent)
        }
    }

    override fun <IntentMapper> postReturnIntent(processors: Map<Class<out ViewIntent>, Provider<IntentMapper>>, viewIntentProducer: IntentMapper.() -> ViewIntent) {
        postIntent(processors.findProcessor(popTopReturnIntent()).viewIntentProducer())
    }

    override fun propagateParentHolder() {
        currentState.setParentHolder(this)
    }

    override fun setParentHolder(parentHolder: ViewStateHolder) {
        this.parentHolder = parentHolder as ViewStateHolderImpl
        this.viewModel = parentHolder.viewModel
        if(this::viewHolderScope.isInitialized) viewHolderScope.cancel()
        initializeCoroutineScope(parentHolder.viewHolderScope)
        propagateParentHolder()
    }

    override fun start() {
        viewModel.registerHolder(this)
        intentJob = viewHolderScope.launch {
            mutableViewIntents.collect { intent ->
                processIntent(intent)
            }
        }
        currentState.let { theState ->
            theState.start()
            initializers.findProcessorOrNull(theState)?.initialize(theState, this)
        }
    }

    override fun stop() {
        currentState.stop()
        _stateCoroutineScope?.run {
            cancel()
            _stateCoroutineScope = null
        }
        viewHolderScope.coroutineContext.cancelChildren()
        intentJob = null
        while(operationsStack.isNotEmpty()) {
            val size = operationsStack.size
            backStack.remove(operationsStack.first().uuid)
            if(size == operationsStack.size) error("Backstack is corrupted")
        }
        viewModel.unregisterHolder(this)
    }

    @Composable
    override fun Visualize(modifier: Modifier) {
        mutableViewStates.collectAsState().value.let { state -> visualizers.findProcessor(state).Visualize(modifier, state, this) }
    }

    private suspend fun processIntent(intent: ViewIntent) {
        val oldState = viewStates.value

        val newState = try {
            reducers.findProcessor(intent)
                .reduce(
                    intent = intent,
                    state = oldState,
                    stateHolder = this
                )
        } catch(e: CancellationException) {
            throw e
        } catch(e: Exception) {
            logError("Unhandled exception processing intent ${intent::class.simpleName}", e)
            return
        }

        if(newState !== oldState) {
            oldState.stop()
            _stateCoroutineScope?.run {
                cancel()
                _stateCoroutineScope = null
            }
            newState.setParentHolder(this@ViewStateHolderImpl)
            newState.start()
            initializers.findProcessorOrNull(newState)?.initialize(newState, this)
            mutableViewStates.emit(newState)
            logInfo("View state emitted: $newState")
        }
    }

    override fun pushReturnIntent(viewIntent: ViewIntent) {
        val operation = BackStackEntry(UUID.randomUUID(), viewIntent)
        operationsStack.add(operation)
        viewModel.backStack.push(this, operation.uuid)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        with(parcel) {
            writeParcelable(ParcelUuid(globalId), flags)
            writeParcelable(mutableViewStates.value, flags)
            writeParcelableArray(operationsStack.toTypedArray(), flags)
            writeString(tag)
        }
    }

    companion object CREATOR : Parcelable.Creator<ViewStateHolderImpl?> {
        override fun createFromParcel(parcel: Parcel?): ViewStateHolderImpl? = parcel?.let { ViewStateHolderImpl(it) }
        override fun newArray(size: Int): Array<ViewStateHolderImpl?> = arrayOfNulls(size)
    }
}