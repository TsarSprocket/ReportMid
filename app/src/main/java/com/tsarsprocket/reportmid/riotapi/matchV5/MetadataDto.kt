package com.tsarsprocket.reportmid.riotapi.matchV5

import com.google.gson.annotations.SerializedName

data class MetadataDto(
    @SerializedName("dataVersion") val dataVersion: String,
    @SerializedName("matchId") val matchId: String,
    @SerializedName("participants") val participants: List<String>,
)
