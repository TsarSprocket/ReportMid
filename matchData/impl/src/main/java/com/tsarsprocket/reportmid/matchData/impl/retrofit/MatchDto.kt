package com.tsarsprocket.reportmid.matchData.impl.retrofit

import com.google.gson.annotations.SerializedName

internal data class MatchDto(
    @SerializedName("info") val info: InfoDto,
)
