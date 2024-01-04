package com.tsarsprocket.reportmid.data_dragon_impl.retrofit

 import com.google.gson.annotations.SerializedName

data class ItemsDto(
    @SerializedName("data") val data: Map<String, ItemDataDto>,
)

