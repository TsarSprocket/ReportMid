package com.tsarsprocket.reportmid.riotapi.ddragon

 import com.google.gson.annotations.SerializedName

data class ItemsDto (
    @SerializedName("data") val data: Map<String,ItemDataDto>,
)

