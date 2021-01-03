package com.tsarsprocket.reportmid.riotapi.spectatorV4

import com.google.gson.annotations.SerializedName

data class CurrentGameInfo(
    @SerializedName("gameId")               val gameId:             Long,
    @SerializedName("gameType")             val gameType:           String,
    @SerializedName("gameStartTime")        val gameStartTime:      Long,
    @SerializedName("mapId")                val mapId:              Long,
    @SerializedName("gameLength")           val gameLength:         Long,
    @SerializedName("platformId")           val platformId:         String,
    @SerializedName("gameMode")             val gameMode:           String,
    @SerializedName("bannedChampions")      val bannedChampions:    List<BannedChampion>,
    @SerializedName("gameQueueConfigId")    val gameQueueConfigId:  Long,
    @SerializedName("observers")            val observers:          Observer,
    @SerializedName("participants")         val participants:       List<CurrentGameParticipant>,
)