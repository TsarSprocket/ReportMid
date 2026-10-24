package com.tsarsprocket.reportmid.viewStateImpl.viewmodel

import com.tsarsprocket.reportmid.viewStateApi.backstack.BackStack
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateImpl.backstack.BackOperation
import kotlinx.coroutines.CoroutineScope
import java.util.UUID

internal interface InternalViewStateHolder : ViewStateHolder {
    var viewModel: ViewStateViewModel
    fun doGoBack(uuid: UUID = lastOperationUuid)
    override var parentHolder: InternalViewStateHolder?

    /**
     * Looks up the [ViewIntent] of the operation identified by [uuid] on this holder's local stack, without
     * removing it. Used by the global back stack to expose [BackStack.entriesFlow] without keeping its own
     * copy of each operation's [ViewIntent].
     */
    fun goBackIntentFor(uuid: UUID): ViewIntent?
    fun initializeCoroutineScope(scope: CoroutineScope)
    fun removeOperation(uuid: UUID): BackOperation?

    /**
     * Removes the operation identified by [uuid] from this holder's own local stack only, without notifying
     * the global back stack. Used by [com.tsarsprocket.reportmid.viewStateImpl.backstack.InternalBackStack.removeNoCheck]
     * to clear this holder's external reference to an operation that was already removed on the back stack's
     * side, so the two don't drift out of sync. A no-op if [uuid] is not present on this holder's stack.
     */
    fun discardOperation(uuid: UUID)
    fun propagateParentHolder()
    fun postIntentToSelf(intent: ViewIntent, returnIntent: ViewIntent?)
}