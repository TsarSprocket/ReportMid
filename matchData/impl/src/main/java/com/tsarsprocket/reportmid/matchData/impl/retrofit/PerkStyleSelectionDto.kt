package com.tsarsprocket.reportmid.matchData.impl.retrofit

import com.google.gson.annotations.SerializedName

data class PerkStyleSelectionDto(
    @SerializedName("perk") val perk: Int,
    @SerializedName("var1") val var1: Int,
    @SerializedName("var2") val var2: Int,
    @SerializedName("var3") val var3: Int,
)
