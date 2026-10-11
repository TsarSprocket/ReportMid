package com.tsarsprocket.reportmid.dataDragonImpl.retrofit

import com.google.gson.annotations.SerializedName

data class SpellDataDto(
    @SerializedName("id") val id: String,
    @SerializedName("image") val image: SpellImageDto,
    @SerializedName("key") val key: String,
    @SerializedName("name") val name: String,
)
