package com.tsarsprocket.reportmid.utils.compose.loadable

import androidx.compose.runtime.Immutable

/** Represents all possible states of a [LoadableHandle]. */
@Immutable
sealed interface LoadState<out T> {

    /** Data is being fetched; no result is available yet. */
    @Immutable
    data object Loading : LoadState<Nothing>

    /** Data was fetched successfully. */
    @Immutable
    data class Loaded<T>(val data: T) : LoadState<T>

    /** Fetching failed with [throwable]. The load can be retried via [LoadableHandle.reload]. */
    @Immutable
    data class Failed(val throwable: Throwable) : LoadState<Nothing>
}
