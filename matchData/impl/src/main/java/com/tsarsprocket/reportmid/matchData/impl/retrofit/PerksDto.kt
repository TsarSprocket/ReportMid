package com.tsarsprocket.reportmid.matchData.impl.retrofit

import com.google.gson.annotations.SerializedName

internal data class PerksDto(
    @SerializedName("statPerks") val statPerks: PerkStatsDto,
    @SerializedName("styles") val styles: List<PerkStyleDto>
)
