package com.tsarsprocket.reportmid.riotapi.ddragon
import com.google.gson.annotations.SerializedName

data class SummonerSpellsDto(
    @SerializedName("data") val data: Map<String,SpellDataDto>,
    @SerializedName("type") val type: String,
    @SerializedName("version") val version: String
)
