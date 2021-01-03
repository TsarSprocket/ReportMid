package com.tsarsprocket.reportmid.riotapi.spectatorV4

import com.google.gson.annotations.SerializedName

data class Perks(
    @SerializedName("perkIds")      val perkIds:        List<Long>,
    @SerializedName("perkStyle")    val perkStyle:      Long,
    @SerializedName("perkSubStyle") val perkSubStyle:   Long,
)
