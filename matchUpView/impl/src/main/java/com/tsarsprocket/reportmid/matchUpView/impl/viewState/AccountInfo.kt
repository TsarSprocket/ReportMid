package com.tsarsprocket.reportmid.matchUpView.impl.viewState

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class AccountInfo(
    val name: String,
) : Parcelable
