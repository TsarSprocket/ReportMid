package com.tsarsprocket.reportmid.viewStateImpl.backstack

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
internal data class OpRef(
    val holderUUID: UUID,
    var down: UUID?,
    var up: UUID? = null,
) : Parcelable