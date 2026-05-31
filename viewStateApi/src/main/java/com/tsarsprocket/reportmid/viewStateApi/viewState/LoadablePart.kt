package com.tsarsprocket.reportmid.viewStateApi.viewState

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.coroutines.Job
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

sealed interface LoadablePart<out T : Parcelable> : Parcelable {

    @Parcelize
    data object NotLoaded : LoadablePart<Nothing>

    @Parcelize
    data object Loading : LoadablePart<Nothing>

    @Parcelize
    data object Failed : LoadablePart<Nothing>

    @Parcelize
    data class Loaded<out T : Parcelable>(
        val data: T,
    ) : LoadablePart<T>
}
