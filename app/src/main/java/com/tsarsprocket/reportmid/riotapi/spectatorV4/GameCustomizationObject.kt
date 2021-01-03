package com.tsarsprocket.reportmid.riotapi.spectatorV4

import com.google.gson.annotations.SerializedName

data class GameCustomizationObject(
    @SerializedName("category") val category:   String,
    @SerializedName("content")  val content:    String,
)
