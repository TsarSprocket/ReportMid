package com.tsarsprocket.reportmid.lol.api.domain

import com.tsarsprocket.reportmid.lol.api.domain.model.GameType

fun interface GameTypeFactory {

    fun getGameType(
        gameMode: String,
        gameType: String,
        mapId: Int,
        queueId: Int,
    ): GameType
}