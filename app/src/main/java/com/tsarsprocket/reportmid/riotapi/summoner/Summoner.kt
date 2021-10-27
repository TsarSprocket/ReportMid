package com.tsarsprocket.reportmid.riotapi.summoner
import com.google.gson.annotations.SerializedName


data class Summoner(
    @SerializedName("accountId") val accountId: String,
    @SerializedName("profileIconId") val profileIconId: Int,
    @SerializedName("revisionDate") val revisionDate: Long,
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: String,
    @SerializedName("puuid") val puuid: String,
    @SerializedName("summonerLevel") val summonerLevel: Long,
)
