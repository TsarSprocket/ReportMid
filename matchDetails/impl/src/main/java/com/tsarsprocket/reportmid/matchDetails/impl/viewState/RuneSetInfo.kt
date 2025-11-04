package com.tsarsprocket.reportmid.matchDetails.impl.viewState

import android.os.Parcelable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class RuneSetInfo(
    val primaryRune: RuneInfo,
    val primaryPath: ImmutableList<RuneInfo>,
    val secondaryPath: ImmutableList<RuneInfo>
) : Parcelable
