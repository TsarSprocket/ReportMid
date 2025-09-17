package com.tsarsprocket.reportmid.findSummonerImpl.domain

import android.os.Parcelable
import com.tsarsprocket.reportmid.lol.api.model.Region
import kotlinx.parcelize.Parcelize

@Parcelize
data class SummonerData(
    val puuid: String,
    val region: Region,
    val gameName: String,
    val tagLine: String,
    val iconUrl: String,
    val level: Int,
) : Parcelable