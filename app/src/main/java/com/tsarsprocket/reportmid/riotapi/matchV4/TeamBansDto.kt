package com.tsarsprocket.reportmid.riotapi.matchV4

import com.google.gson.annotations.SerializedName

data class TeamBansDto(
    @SerializedName("championId") val championId: Int,
    @SerializedName("pickTurn") val pickTurn: Int,
)