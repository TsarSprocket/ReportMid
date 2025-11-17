package com.tsarsprocket.reportmid.matchDetails.impl.viewState

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class SummonerSpellInfo(
    val iconUrl: String,
    val name: String,
) : Parcelable
