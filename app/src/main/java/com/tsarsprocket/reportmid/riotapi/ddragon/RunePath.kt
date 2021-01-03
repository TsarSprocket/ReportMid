package com.tsarsprocket.reportmid.riotapi.ddragon

import com.google.gson.annotations.SerializedName

data class RunePath(
    @SerializedName("icon") val icon: String,
    @SerializedName("id") val id: Int,
    @SerializedName("key") val key: String,
    @SerializedName("name") val name: String,
    @SerializedName("slots") val slots: List<RuneSlot>
)
