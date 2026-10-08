package com.tsarsprocket.reportmid.viewStateImpl.viewmodel

import com.tsarsprocket.reportmid.viewStateApi.backstack.BackStack
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateImpl.backstack.BackOperation
import kotlinx.coroutines.CoroutineScope
import java.util.UUID

interface InternalViewStateHolder : ViewStateHolder {
    fun doGoBack(uuid: UUID = lastOperationUuid)

    /**
     * Looks up the [ViewIntent] of the operation identified by [uuid] on this holder's local stack, without
     * removing it. Used by the global back stack to expose [BackStack.entriesFlow] without keeping its own
     * copy of each operation's [ViewIntent].
     */
    fun goBackIntentFor(uuid: UUID): ViewIntent?
    fun initializeCoroutineScope(scope: CoroutineScope)
    fun removeOperation(uuid: UUID): BackOperation?
}