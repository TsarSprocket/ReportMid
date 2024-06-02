package com.tsarsprocket.reportmid.dataDragonImpl.retrofit

import com.google.gson.annotations.SerializedName

data class RuneSlotDto(
    @SerializedName("runes") val runes: List<RuneDto>
)