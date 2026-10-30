package com.tsarsprocket.reportmid.viewStateApi.backstack

import android.os.Parcelable
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import kotlinx.parcelize.Parcelize
import java.util.UUID

/**
 * An immutable snapshot of a single back operation currently present on the back stack.
 *
 * [uuid] uniquely identifies the operation and can be passed to the back navigation mechanism
 * to unwind the stack down to this entry.
 */
@Parcelize
data class BackStackEntry(
    val uuid: UUID,
    val viewIntent: ViewIntent,
) : Parcelable
