package com.tsarsprocket.reportmid.currentGameData.impl.retrofit.dto

import com.google.gson.annotations.SerializedName

internal data class PerksDto(
    @SerializedName("perkIds") val perkIds: List<Long>,
    @SerializedName("perkStyle") val perkStyle: Long,
    @SerializedName("perkSubStyle") val perkSubStyle: Long,
)
