package com.tsarsprocket.reportmid.riotapi.matchV5

import com.google.gson.annotations.SerializedName

data class PerkStyleDto (
    @SerializedName("description") val description: String,
    @SerializedName("selections") val selections: List<PerkStyleSelectionDto>,
    @SerializedName("style") val style: Int,
) {
    enum class Description(val value: String) { PRIMARY_STYLE("primaryStyle"), SUBSTYLE("subStyle") }
}