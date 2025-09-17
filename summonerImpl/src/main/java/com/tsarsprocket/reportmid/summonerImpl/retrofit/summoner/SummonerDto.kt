package com.tsarsprocket.reportmid.summonerImpl.retrofit.summoner
import com.google.gson.annotations.SerializedName


data class SummonerDto(
    @SerializedName("profileIconId") val profileIconId: Int,
    @SerializedName("revisionDate") val revisionDate: Long,
    @SerializedName("puuid") val puuid: String,
    @SerializedName("summonerLevel") val summonerLevel: Int,
)
