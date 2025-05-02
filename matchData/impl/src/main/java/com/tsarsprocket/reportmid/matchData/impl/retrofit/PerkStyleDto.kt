package com.tsarsprocket.reportmid.matchData.impl.retrofit

import com.google.gson.annotations.SerializedName

data class PerkStyleDto(
    @SerializedName("description") val description: String,
    @SerializedName("selections") val selections: List<PerkStyleSelectionDto>,
    @SerializedName("style") val style: Int,
)
