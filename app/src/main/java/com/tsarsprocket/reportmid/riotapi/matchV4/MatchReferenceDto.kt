package com.tsarsprocket.reportmid.riotapi.matchV4

import com.google.gson.annotations.SerializedName

data class MatchReferenceDto(
    @SerializedName("gameId") val gameId: Long,
    @SerializedName("role") val role: String,
    @SerializedName("season") val season: Int,
    @SerializedName("platformId") val platformId: String,
    @SerializedName("champion") val champion: Int,
    @SerializedName("queue") val queue: Int,
    @SerializedName("lane") val lane: String,
    @SerializedName("timestamp") val timestamp: Long,
)
