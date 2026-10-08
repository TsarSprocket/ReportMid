package com.tsarsprocket.reportmid.viewStateImpl.backstack

import com.tsarsprocket.reportmid.viewStateApi.backstack.BackStack
import com.tsarsprocket.reportmid.viewStateImpl.viewmodel.InternalViewStateHolder
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

interface InternalBackStack : BackStack {

    val stackSize: StateFlow<Int>

    /**
     * Defaults to resolving nothing so that entries computed before this is wired up (e.g. while this instance
     * is still being deserialized) don't crash; reassigning it (done once holders are registered) automatically
     * refreshes [entriesFlow] against the now-resolvable holders.
     */
    var holderResolver: UUID.() -> InternalViewStateHolder?

    /**
     * Returns the operation UUIDs ordered from the top of the stack down to and including [uuid].
     * Returns an empty list if [uuid] is not currently present on the stack.
     */
    fun operationIdsFromTopTo(uuid: UUID): List<UUID>

    /**
     * Returns the [ViewStateHolderImpl.globalId] of the holder that owns the operation identified by [uuid],
     * or `null` if no such operation is currently on the stack.
     */
    fun holderIdFor(uuid: UUID): UUID?

    /**
     * Removes operation assuming the holder did it already on its side
     */
    fun removeOperation(uuid: UUID)
}