package com.tsarsprocket.reportmid.riotapi.spectatorV4

import com.google.gson.annotations.SerializedName

data class BannedChampion(
    @SerializedName("pickTurn")     val pickTurn:   Int,
    @SerializedName("championId")   val championId: Long,
    @SerializedName("teamId")       val teamId:     Long,
)
