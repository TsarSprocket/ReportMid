package com.tsarsprocket.reportmid.matchUpView.impl.domain.model

import com.tsarsprocket.reportmid.lol.api.domain.model.Champion
import com.tsarsprocket.reportmid.lol.api.domain.model.CurrentRunes
import com.tsarsprocket.reportmid.lol.api.domain.model.SummonerSpell

internal data class Participant(
    val puuid: String?,
    val champion: Champion,
    val profileIcon: String,
    val isBot: Boolean,
    val runes: CurrentRunes,
    val summonerSpell1: SummonerSpell,
    val summonerSpell2: SummonerSpell,
)
