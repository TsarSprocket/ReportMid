package com.tsarsprocket.reportmid.dataDragonImpl.retrofit

import com.google.gson.annotations.SerializedName

data class ItemDataDto (
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: ItemImageDto,
)
