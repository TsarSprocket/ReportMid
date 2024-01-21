package com.tsarsprocket.reportmid.summoner_impl.retrofit.championMastery

import com.google.gson.annotations.SerializedName

data class ChampionMasteryDto(
    @SerializedName("championPointsUntilNextLevel") val championPointsUntilNextLevel: Long,
    @SerializedName("chestGranted") val chestGranted: Boolean,
    @SerializedName("championId") val championId: Long,
    @SerializedName("lastPlayTime") val lastPlayTime: Long,
    @SerializedName("championLevel") val championLevel: Int,
    @SerializedName("summonerId") val summonerId: String,
    @SerializedName("championPoints") val championPoints: Int,
    @SerializedName("championPointsSinceLastLevel") val championPointsSinceLastLevel: Long,
    @SerializedName("tokensEarned") val tokensEarned: Int,
)
