package com.tsarsprocket.reportmid.data_dragon_impl.retrofit
import com.google.gson.annotations.SerializedName

data class SummonerSpellsDto(
    @SerializedName("data") val data: Map<String, SpellDataDto>,
    @SerializedName("type") val type: String,
    @SerializedName("version") val version: String
)
