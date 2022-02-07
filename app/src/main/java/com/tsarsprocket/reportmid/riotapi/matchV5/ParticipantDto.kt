package com.tsarsprocket.reportmid.riotapi.matchV5

import com.google.gson.annotations.SerializedName

data class ParticipantDto(
    @SerializedName("assists") val assists: Int,
    @SerializedName("championId") val championId: Int,
    @SerializedName("deaths") val deaths: Int,
    @SerializedName("item0") val item0: Int,
    @SerializedName("item1") val item1: Int,
    @SerializedName("item2") val item2: Int,
    @SerializedName("item3") val item3: Int,
    @SerializedName("item4") val item4: Int,
    @SerializedName("item5") val item5: Int,
    @SerializedName("item6") val item6: Int,
    @SerializedName("kills") val kills: Int,
    @SerializedName("participantId") val participantId: Int,
    @SerializedName("perks") val perks: PerksDto,
    @SerializedName("puuid" )val puuid: String,
    @SerializedName("summoner1Id") val summoner1Id: Long,
    @SerializedName("summoner2Id") val summoner2Id: Long,
    @SerializedName("teamId") val teamId: Int, // 100 - blue, 200 - red
    @SerializedName("totalMinionsKilled") val totalMinionsKilled: Int,
    @SerializedName("win") val win: Boolean,
)