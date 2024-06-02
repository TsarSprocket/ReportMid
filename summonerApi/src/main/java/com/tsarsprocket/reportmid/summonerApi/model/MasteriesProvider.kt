package com.tsarsprocket.reportmid.summonerApi.model

fun interface MasteriesProvider {
    suspend fun get(): List<ChampionMastery>
}