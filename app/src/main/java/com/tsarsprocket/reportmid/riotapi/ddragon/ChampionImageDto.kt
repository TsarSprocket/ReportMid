package com.tsarsprocket.reportmid.riotapi.ddragon

import com.google.gson.annotations.SerializedName

data class ChampionImageDto(
    @SerializedName("full") val full: String,
    @SerializedName("group") val group: String,
    @SerializedName("h") val h: Int,
    @SerializedName("sprite") val sprite: String,
    @SerializedName("w") val w: Int,
    @SerializedName("x") val x: Int,
    @SerializedName("y") val y: Int
)