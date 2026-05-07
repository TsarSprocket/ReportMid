package com.tsarsprocket.reportmid.currentGameData.impl.retrofit.dto

import com.google.gson.annotations.SerializedName

internal data class CurrentGameInfoDto(
    @SerializedName("gameId") val gameId: Long,
    @SerializedName("gameType") val gameType: String,
    @SerializedName("gameStartTime") val gameStartTime: Long,
    @SerializedName("mapId") val mapId: Int,
    @SerializedName("gameMode") val gameMode: String,
    @SerializedName("bannedChampions") val bannedChampions: List<BannedChampionDto>,
    @SerializedName("gameQueueConfigId") val gameQueueConfigId: Int,
    @SerializedName("participants") val participants: List<CurrentGameParticipantDto>,
) : SpectatorResponse