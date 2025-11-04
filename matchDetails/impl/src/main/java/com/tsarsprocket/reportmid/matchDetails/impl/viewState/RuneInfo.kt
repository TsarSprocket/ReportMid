package com.tsarsprocket.reportmid.matchDetails.impl.viewState

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
internal data class RuneInfo(
    val icon: String,
    val name: String,
) : Parcelable
