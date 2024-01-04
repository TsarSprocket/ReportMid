package com.tsarsprocket.reportmid.data_dragon_impl.retrofit

import com.google.gson.annotations.SerializedName

data class SpellDataDto(
    @SerializedName("cooldown") val cooldown: List<Double>,
    @SerializedName("cooldownBurn") val cooldownBurn: String,
    @SerializedName("cost") val cost: List<Int>,
    @SerializedName("costBurn") val costBurn: String,
    @SerializedName("costType") val costType: String,
    @SerializedName("description") val description: String,
    @SerializedName("effect") val effect: List<List<Double>?>,
    @SerializedName("effectBurn") val effectBurn: List<Double?>,
    @SerializedName("id") val id: String,
    @SerializedName("image") val image: SpellImageDto,
    @SerializedName("key") val key: String,
    @SerializedName("maxammo") val maxammo: String,
    @SerializedName("maxrank") val maxrank: Int,
    @SerializedName("modes") val modes: List<String>,
    @SerializedName("name") val name: String,
    @SerializedName("range") val range: List<Int>,
    @SerializedName("rangeBurn") val rangeBurn: String,
    @SerializedName("resource") val resource: String,
    @SerializedName("summonerLevel") val summonerLevel: Int,
    @SerializedName("tooltip") val tooltip: String,
)
