package com.tsarsprocket.reportmid.viewStateImpl.backstack

import com.tsarsprocket.reportmid.viewStateImpl.viewState.InternalViewStateHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

internal class BackStack {

    private var top: OpRef? = null
    private val allOpRefs = mutableMapOf<UUID, OpRef>()
    private val stackSizePublisher = MutableStateFlow(allOpRefs.size)

    val stackSize: StateFlow<Int>
        get() = stackSizePublisher.asStateFlow()

    fun goBack() {
        top?.holder?.doGoBack()
    }

    fun push(holder: InternalViewStateHolder, uuid: UUID) {
        val opRef = OpRef(holder, top)
        allOpRefs[uuid] = opRef
        top?.run { up = opRef }
        top = opRef
        stackSizePublisher.value = allOpRefs.size
    }

    /**
     * Removes operation assuming the holder did it already on its side
     */
    fun removeOperation(uuid: UUID) {
        allOpRefs.remove(uuid)?.let { opRef ->
            if(top === opRef) top = opRef.down
            opRef.down?.run { up = opRef.up }
            opRef.up?.run { down = opRef.down }
        }
    }

    class OpRef(val holder: InternalViewStateHolder, var down: OpRef?) {
        var up: OpRef? = null
    }
}