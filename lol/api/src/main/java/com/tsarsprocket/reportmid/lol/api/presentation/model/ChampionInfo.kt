package com.tsarsprocket.reportmid.lol.api.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChampionInfo(
    val icon: String,
    val name: String,
) : Parcelable