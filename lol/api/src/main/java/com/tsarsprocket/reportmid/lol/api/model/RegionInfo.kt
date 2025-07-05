package com.tsarsprocket.reportmid.lol.api.model

data class RegionInfo(
    val regionId: Int,
    val title: String,
) {

    interface Factory {
        fun get(regionId: Int): RegionInfo
    }
}