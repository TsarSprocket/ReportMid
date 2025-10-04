package com.tsarsprocket.reportmid.lol.api.model

fun interface GameTypeFactory {

    fun getGameType(
        gameMode: String,
        gameType: String,
        mapId: Int,
        queueId: Int,
    ): GameType
}