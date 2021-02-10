package com.tsarsprocket.reportmid.riotapi.matchV4

import com.google.gson.annotations.SerializedName

data class RuneDto(
    @SerializedName("runeId") val runeId: Int,
    @SerializedName("rank") val rank: Int,
)