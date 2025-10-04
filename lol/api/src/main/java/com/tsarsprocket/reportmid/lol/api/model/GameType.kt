package com.tsarsprocket.reportmid.lol.api.model

data class GameType(
    val gameMode: String,
    val gameType: String,
    val mapId: Int,
    val queueId: Int,
    val name: String,
) {
    val isSummonerRift
        get() = mapId == MAP_ID_SUMMONER_RIFT
}
