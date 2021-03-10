package com.tsarsprocket.reportmid.riotapi.ddragon

import com.google.gson.annotations.SerializedName

data class AllChampionsDto(
    @SerializedName("data") val data: Map<String,ChampionDto>,
    @SerializedName("format") val format: String,
    @SerializedName("type") val type: String,
    @SerializedName("version") val version: String,
)
