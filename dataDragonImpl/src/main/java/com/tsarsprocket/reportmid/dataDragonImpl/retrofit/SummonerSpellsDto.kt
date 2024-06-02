package com.tsarsprocket.reportmid.dataDragonImpl.retrofit
import com.google.gson.annotations.SerializedName

data class SummonerSpellsDto(
    @SerializedName("data") val data: Map<String, SpellDataDto>,
    @SerializedName("type") val type: String,
    @SerializedName("version") val version: String
)
