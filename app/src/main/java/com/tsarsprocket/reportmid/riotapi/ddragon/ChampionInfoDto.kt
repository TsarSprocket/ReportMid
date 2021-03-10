package com.tsarsprocket.reportmid.riotapi.ddragon

import com.google.gson.annotations.SerializedName

data class ChampionInfoDto(
    @SerializedName("attack") val attack: Int,
    @SerializedName("defense") val defense: Int,
    @SerializedName("difficulty") val difficulty: Int,
    @SerializedName("magic") val magic: Int
)
