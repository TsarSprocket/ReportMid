package com.tsarsprocket.reportmid.viewStateApi.backstack

import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import java.util.UUID

/**
 * An immutable snapshot of a single back operation currently present on the back stack.
 *
 * [uuid] uniquely identifies the operation and can be passed to the back navigation mechanism
 * to unwind the stack down to this entry.
 */
interface BackStackEntry {
    val uuid: UUID
    val viewIntent: ViewIntent
}
