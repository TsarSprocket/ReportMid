package com.tsarsprocket.reportmid.dataDragonImpl.retrofit

 import com.google.gson.annotations.SerializedName

data class ItemsDto(
    @SerializedName("data") val data: Map<Int, ItemDataDto>,
)

