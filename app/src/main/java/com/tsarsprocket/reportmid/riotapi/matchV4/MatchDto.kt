package com.tsarsprocket.reportmid.riotapi.matchV4

import com.google.gson.annotations.SerializedName

data class MatchDto(
    @SerializedName("gameId") val gameId: Long,
    @SerializedName("participantIdentities") val participantIdentities: List<ParticipantIdentityDto>,
    @SerializedName("queueId") val queueId: Int,
    @SerializedName("gameType") val gameType: String,
    @SerializedName("gameDuration") val gameDuration: Long,
    @SerializedName("teams") val teams: List<TeamStatsDto>,
    @SerializedName("platformId") val platformId: String,
    @SerializedName("gameCreation") val gameCreation: Long,
    @SerializedName("seasonId") val seasonId: Int,
    @SerializedName("gameVersion") val gameVersion: String,
    @SerializedName("mapId") val mapId: Int,
    @SerializedName("gameMode") val gameMode: String,
    @SerializedName("participants") val participants: List<ParticipantDto>,
)
