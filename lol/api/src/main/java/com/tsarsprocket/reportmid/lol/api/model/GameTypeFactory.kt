package com.tsarsprocket.reportmid.lol.api.model

fun interface GameTypeFactory {

    fun getGameType(
        gameMode: String,
        gameType: String,
        mapId: String,
        queueId: Int,
    ): GameType
}