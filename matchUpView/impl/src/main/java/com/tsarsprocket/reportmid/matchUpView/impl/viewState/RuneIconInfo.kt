package com.tsarsprocket.reportmid.matchUpView.impl.viewState

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RuneIconInfo(
    val imageUrl: String,
    val title: String,
) : Parcelable
