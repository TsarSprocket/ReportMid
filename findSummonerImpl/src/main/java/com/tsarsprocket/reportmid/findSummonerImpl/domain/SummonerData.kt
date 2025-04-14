package com.tsarsprocket.reportmid.findSummonerImpl.domain

import android.os.Parcelable
import com.tsarsprocket.reportmid.lol.model.Puuid
import com.tsarsprocket.reportmid.lol.model.Region
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class SummonerData(
    val puuid: @RawValue Puuid,
    val region: Region,
    val riotId: String,
    val iconUrl: String,
    val level: Int,
) : Parcelable