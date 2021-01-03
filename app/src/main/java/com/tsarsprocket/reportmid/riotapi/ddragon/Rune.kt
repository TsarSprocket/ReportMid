package com.tsarsprocket.reportmid.riotapi.ddragon

import com.google.gson.annotations.SerializedName

data class Rune(
    @SerializedName("icon") val icon: String,
    @SerializedName("id") val id: Int,
    @SerializedName("key") val key: String,
    @SerializedName("longDesc") val longDesc: String,
    @SerializedName("name") val name: String,
    @SerializedName("shortDesc") val shortDesc: String
)