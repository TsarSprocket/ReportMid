package com.tsarsprocket.reportmid.riotapi.matchV5

import com.google.gson.annotations.SerializedName

data class InfoDto(
    @SerializedName("gameDuration") val gameDuration: Long,
    @SerializedName("gameMode") val gameMode: String,
    @SerializedName("gameType") val gameType: String,
    @SerializedName("mapId") val mapId: Int,
    @SerializedName("participants") val participants: List<ParticipantDto>,
    @SerializedName("queueId") val queueId: Int,
)
