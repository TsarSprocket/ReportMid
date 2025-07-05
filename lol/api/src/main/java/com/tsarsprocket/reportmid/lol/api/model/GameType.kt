package com.tsarsprocket.reportmid.lol.api.model

data class GameType(
    val gameMode: String,
    val gameType: String,
    val mapId: String,
    val queueId: Int,
    val name: String,
)
