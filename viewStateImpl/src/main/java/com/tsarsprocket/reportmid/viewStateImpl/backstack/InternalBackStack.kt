package com.tsarsprocket.reportmid.viewStateImpl.backstack

import com.tsarsprocket.reportmid.viewStateApi.backstack.BackStack
import com.tsarsprocket.reportmid.viewStateImpl.viewmodel.InternalViewStateHolder
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

internal interface InternalBackStack : BackStack {

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
     * Discards operation assuming the holder did it already on its side
     */
    fun discardOperation(uuid: UUID)

    /**
     * Removes exactly the single operation identified by [uuid] from the back stack, without assuming the
     * owning holder has already removed its own local reference to it (unlike [discardOperation]).
     *
     * The back stack's own integrity is maintained regardless: the surrounding entries are relinked and
     * [top] is updated if [uuid] was on top. In addition, if the owning holder can still be resolved and
     * still holds a local reference to the operation, that external reference is cleared as well, so callers
     * don't need to check for or clear it themselves. A no-op if [uuid] is not currently present on the stack.
     */
    fun removeNoCheck(uuid: UUID)
}