package com.tsarsprocket.reportmid.viewStateImpl.backstack

import android.os.Parcelable
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
class BackOperation(
    val goBackIntent: ViewIntent,
    val uuid: UUID = UUID.randomUUID()
) : Parcelable
