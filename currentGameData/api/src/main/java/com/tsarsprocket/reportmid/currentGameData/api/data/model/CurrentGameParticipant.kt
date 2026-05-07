package com.tsarsprocket.reportmid.currentGameData.api.data.model

import com.tsarsprocket.reportmid.lol.api.domain.model.SummonerSpell

data class CurrentGameParticipant(
    val puuid: String,
    val championId: Long,
    val profileIcon: String,
    val isBot: Boolean,
    val runes: Runes,
    val summonerSpell1: SummonerSpell,
    val summonerSpell2: SummonerSpell,
)
