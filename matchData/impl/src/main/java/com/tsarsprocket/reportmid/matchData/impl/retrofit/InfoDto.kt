package com.tsarsprocket.reportmid.matchData.impl.retrofit

import com.google.gson.annotations.SerializedName

internal data class InfoDto(
    @SerializedName("gameEndTimestamp") val gameEndTimestamp: Long,
    @SerializedName("gameStartTimestamp") val gameStartTimestamp: Long,
    @SerializedName("gameMode") val gameMode: String,
    @SerializedName("gameType") val gameType: String,
    @SerializedName("mapId") val mapId: Int,
    @SerializedName("participants") val participants: List<ParticipantDto>,
    @SerializedName("platformId") val platformId: String,
    @SerializedName("queueId") val queueId: Int,
    @SerializedName("teams") val teams: List<TeamDto>,
)
