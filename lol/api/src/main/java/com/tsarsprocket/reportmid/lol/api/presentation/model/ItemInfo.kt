package com.tsarsprocket.reportmid.lol.api.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface ItemInfo : Parcelable {

    @Parcelize
    data class Known(
        val icon: String,
        val name: String,
    ) : ItemInfo

    @Parcelize
    data object Unknown : ItemInfo

    @Parcelize
    data object Empty : ItemInfo
}