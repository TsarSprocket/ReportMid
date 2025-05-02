package com.tsarsprocket.reportmid.matchData.impl.retrofit

import com.google.gson.annotations.SerializedName

data class PerkStatsDto(
    @SerializedName("defense") val defense: Int,
    @SerializedName("flex") val flex: Int,
    @SerializedName("offense") val offense: Int,
)
