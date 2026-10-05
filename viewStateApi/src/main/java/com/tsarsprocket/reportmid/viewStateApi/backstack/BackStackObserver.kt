package com.tsarsprocket.reportmid.viewStateApi.backstack

import kotlinx.coroutines.flow.StateFlow

/**
 * Exposes the current content of the back stack as a reactive, always-consistent snapshot.
 *
 * [entries] always reflects the *full* ordered picture of the back stack rather than incremental
 * diffs: every emission is the complete, up-to-date list, ordered from the top of the stack (the
 * operation that would be undone first) down to the bottom (the oldest operation still pending).
 * This makes the API trivial and safe to consume from UI code - collectors only ever need to look
 * at the latest value, there is no risk of drifting out of sync by missing an intermediate event,
 * and a late subscriber immediately receives the current state instead of having to replay history.
 *
 * All updates happen synchronously on the UI thread, so no additional synchronization is required
 * by consumers.
 */
interface BackStackObserver {
    val entries: StateFlow<List<BackStackEntry>>
}
