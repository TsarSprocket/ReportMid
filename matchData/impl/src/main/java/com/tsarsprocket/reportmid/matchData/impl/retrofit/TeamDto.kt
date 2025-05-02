package com.tsarsprocket.reportmid.matchData.impl.retrofit

import com.google.gson.annotations.SerializedName

data class TeamDto(
    @SerializedName("teamId") val teamId: Int,
    @SerializedName("win") val win: Boolean,
)
