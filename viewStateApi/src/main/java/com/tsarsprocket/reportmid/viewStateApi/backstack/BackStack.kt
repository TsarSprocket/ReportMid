package com.tsarsprocket.reportmid.viewStateApi.backstack

import android.os.Parcelable
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

interface BackStack : Parcelable {

    /**
     * Exposes the current content of the back stack as a reactive, always-consistent snapshot.
     *
     * [entriesFlow] always reflects the *full* ordered picture of the back stack rather than incremental
     * diffs: every emission is the complete, up-to-date list, ordered from the top of the stack (the
     * operation that would be undone first) down to the bottom (the oldest operation still pending).
     * This makes the API trivial and safe to consume from UI code - collectors only ever need to look
     * at the latest value, there is no risk of drifting out of sync by missing an intermediate event,
     * and a late subscriber immediately receives the current state instead of having to replay history.
     *
     * All updates happen synchronously on the UI thread, so no additional synchronization is required
     * by consumers.
     */
    val entriesFlow: StateFlow<List<BackStackEntry>>
    val stackSize: StateFlow<Int>

    /**
     * Defaults to resolving nothing so that entries computed before this is wired up (e.g. while this instance
     * is still being deserialized) don't crash; reassigning it (done once holders are registered) automatically
     * refreshes [entriesFlow] against the now-resolvable holders.
     */
    var holderResolver: UUID.() -> ViewStateHolder?

    fun goBack()

    /**
     * Returns the [ViewStateHolderImpl.globalId] of the holder that owns the operation identified by [uuid],
     * or `null` if no such operation is currently on the stack.
     */
    fun holderIdFor(uuid: UUID): UUID?

    /**
     * Returns the operation UUIDs ordered from the top of the stack down to and including [uuid].
     * Returns an empty list if [uuid] is not currently present on the stack.
     */
    fun operationIdsFromTopTo(uuid: UUID): List<UUID>
    fun push(holder: ViewStateHolder, operationUuid: UUID)

    /**
     * Removes operation assuming the holder did it already on its side
     */
    fun removeOperation(uuid: UUID)
}
