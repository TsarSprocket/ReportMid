package com.tsarsprocket.reportmid.data_dragon_impl.retrofit

import com.google.gson.annotations.SerializedName

data class RuneSlotDto(
    @SerializedName("runes") val runes: List<RuneDto>
)