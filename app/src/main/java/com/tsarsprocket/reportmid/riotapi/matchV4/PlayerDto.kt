package com.tsarsprocket.reportmid.riotapi.matchV4

import com.google.gson.annotations.SerializedName

data class PlayerDto(
    @SerializedName("profileIcon") val profileIcon: Int,
    @SerializedName("accountId") val accountId: String,
    @SerializedName("matchHistoryUri") val matchHistoryUri: String,
    @SerializedName("currentAccountId") val currentAccountId: String,
    @SerializedName("currentPlatformId") val currentPlatformId: String,
    @SerializedName("summonerName") val summonerName: String,
    @SerializedName("summonerId") val summonerId: String,
    @SerializedName("platformId") val platformId: String,
)