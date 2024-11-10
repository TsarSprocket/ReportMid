package com.tsarsprocket.reportmid.summonerImpl.retrofit.account

import com.google.gson.annotations.SerializedName

data class AccountDto(
    @SerializedName("puuid") val puuid: String,
    @SerializedName("gameName") val gameName: String?,
    @SerializedName("tagLine") val tagLine: String?,
)
