package com.tsarsprocket.reportmid.riotapi.ddragon

import com.google.gson.annotations.SerializedName

data class ChampionDto(
    @SerializedName("blurb") val blurb: String,
    @SerializedName("id") val id: String,
    @SerializedName("image") val image: ChampionImageDto,
    @SerializedName("info") val info: ChampionInfoDto,
    @SerializedName("key") val key: Int,
    @SerializedName("name") val name: String,
    @SerializedName("partype") val partype: String,
    @SerializedName("stats") val stats: ChampionStatsDto,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("title") val title: String,
    @SerializedName("version") val version: String,
)
