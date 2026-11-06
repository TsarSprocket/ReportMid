package com.tsarsprocket.reportmid.appBar.impl.viewState

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
internal data class NavigationItem(
    val uuid: UUID,
    val iconUrl: String,
) : Parcelable
