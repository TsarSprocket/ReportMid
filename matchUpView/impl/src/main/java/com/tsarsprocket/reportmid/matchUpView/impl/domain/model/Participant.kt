package com.tsarsprocket.reportmid.matchUpView.impl.domain.model

import com.tsarsprocket.reportmid.lol.api.domain.model.Champion
import com.tsarsprocket.reportmid.lol.api.domain.model.Runes
import com.tsarsprocket.reportmid.lol.api.domain.model.SummonerSpell

internal data class Participant(
    val puuid: String,
    val gameName: String?,
    val tagLine: String?,
    val champion: Champion,
    val profileIcon: String,
    val isBot: Boolean,
    val runes: Runes,
    val summonerSpell1: SummonerSpell,
    val summonerSpell2: SummonerSpell,
)
