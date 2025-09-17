package com.tsarsprocket.reportmid.matchHistory.impl.domain.model

internal data class PlayerData(
    val puuid: String,
    val championIcon: String, // url to image
    val championName: String,
    val kills: Int,
    val deaths: Int,
    val assists: Int,
    val items: List<Int?>,
    val ward: Int?,
)
