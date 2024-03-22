package com.tsarsprocket.reportmid.summoner_api.model

fun interface MasteriesProvider {
    suspend fun get(): List<ChampionMastery>
}