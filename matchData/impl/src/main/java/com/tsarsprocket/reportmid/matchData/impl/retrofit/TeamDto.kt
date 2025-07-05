package com.tsarsprocket.reportmid.matchData.impl.retrofit

import com.google.gson.annotations.SerializedName

internal data class TeamDto(
    @SerializedName("teamId") val teamId: Int,
    @SerializedName("win") val win: Boolean,
)
