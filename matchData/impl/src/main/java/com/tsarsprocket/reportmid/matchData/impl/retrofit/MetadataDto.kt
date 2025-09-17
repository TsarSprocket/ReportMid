package com.tsarsprocket.reportmid.matchData.impl.retrofit

import com.google.gson.annotations.SerializedName

internal data class MetadataDto(
    @SerializedName("matchId") val matchId: String,
)
