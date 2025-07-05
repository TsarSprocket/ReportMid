package com.tsarsprocket.reportmid.matchData.impl.retrofit

import com.google.gson.annotations.SerializedName

internal data class ParticipantDto(
    @SerializedName("assists") val assists: Int,
    @SerializedName("championId") val championId: Int,
    @SerializedName("deaths") val deaths: Int,
    @SerializedName("item0") val item0: Int,
    @SerializedName("item1") val item1: Int,
    @SerializedName("item2") val item2: Int,
    @SerializedName("item3") val item3: Int,
    @SerializedName("item4") val item4: Int,
    @SerializedName("item5") val item5: Int,
    @SerializedName("kills") val kills: Int,
    @SerializedName("perks") val perks: PerksDto,
    @SerializedName("profileIcon") val profileIcon: Int,
    @SerializedName("puuid") val puuid: String,
    @SerializedName("riotIdGameName") val riotIdGameName: String,
    @SerializedName("riotIdTagline") val riotIdTagline: String,
    @SerializedName("summoner1Id") val summoner1Id: Int,
    @SerializedName("summoner2Id") val summoner2Id: Int,
    @SerializedName("teamId") val teamId: Int,
    @SerializedName("teamPosition") val teamPosition: String,
    @SerializedName("visionScore") val visionScore: Int,
    @SerializedName("win") val win: Boolean,
)
