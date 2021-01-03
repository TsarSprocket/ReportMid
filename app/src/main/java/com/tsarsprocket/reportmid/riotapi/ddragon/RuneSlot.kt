package com.tsarsprocket.reportmid.riotapi.ddragon

import com.google.gson.annotations.SerializedName

data class RuneSlot(
    @SerializedName("runes") val runes: List<Rune>
)