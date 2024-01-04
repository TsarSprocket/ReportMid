package com.tsarsprocket.reportmid.data_dragon_impl.retrofit

import com.google.gson.annotations.SerializedName

data class SpellImageDto(
    @SerializedName("full") val full: String,
    @SerializedName("sprite") val sprite: String,
    @SerializedName("group") val group: String,
    @SerializedName("x") val x: Int,
    @SerializedName("y") val y: Int,
    @SerializedName("w") val w: Int,
    @SerializedName("h") val h: Int,
)
