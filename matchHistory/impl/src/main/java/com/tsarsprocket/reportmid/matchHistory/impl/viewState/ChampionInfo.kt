package com.tsarsprocket.reportmid.matchHistory.impl.viewState

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class ChampionInfo(
    val icon: String,
    val name: String,
) : Parcelable
