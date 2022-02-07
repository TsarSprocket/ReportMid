package com.tsarsprocket.reportmid.riotapi.matchV5

import com.google.gson.annotations.SerializedName

data class PerksDto(
    @SerializedName("statPerks") val statPerks: PerkStatsDto,
    @SerializedName("styles") val styles: List<PerkStyleDto>,
)
