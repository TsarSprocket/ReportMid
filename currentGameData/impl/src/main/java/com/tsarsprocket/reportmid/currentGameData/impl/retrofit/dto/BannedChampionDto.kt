package com.tsarsprocket.reportmid.currentGameData.impl.retrofit.dto

import com.google.gson.annotations.SerializedName

internal data class BannedChampionDto(
    @SerializedName("championId") val championId: Int,
    @SerializedName("teamId") val teamId: Int,
)
