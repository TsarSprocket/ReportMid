package com.tsarsprocket.reportmid.riotapi.matchV5

import com.google.gson.annotations.SerializedName

data class MasteryDto(
    @SerializedName("rank") val rank: Int,
    @SerializedName("masteryId") val masteryId: Int,
)