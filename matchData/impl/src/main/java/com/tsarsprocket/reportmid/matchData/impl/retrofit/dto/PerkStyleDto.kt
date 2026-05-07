package com.tsarsprocket.reportmid.matchData.impl.retrofit.dto

import com.google.gson.annotations.SerializedName

internal data class PerkStyleDto(
    @SerializedName("description") val description: String,
    @SerializedName("selections") val selections: List<PerkStyleSelectionDto>,
    @SerializedName("style") val style: Int,
)
