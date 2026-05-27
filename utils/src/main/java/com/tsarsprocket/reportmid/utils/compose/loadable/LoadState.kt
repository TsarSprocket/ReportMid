package com.tsarsprocket.reportmid.utils.compose.loadable

import androidx.compose.runtime.Immutable

@Immutable
sealed interface LoadState<out T> {

    @Immutable
    data object Loading : LoadState<Nothing>

    @Immutable
    data class Loaded<T>(val data: T) : LoadState<T>

    @Immutable
    data class Failed(val throwable: Throwable) : LoadState<Nothing>
}
