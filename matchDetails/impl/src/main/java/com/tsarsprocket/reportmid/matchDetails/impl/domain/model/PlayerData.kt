package com.tsarsprocket.reportmid.matchDetails.impl.domain.model

import com.tsarsprocket.reportmid.lol.api.domain.model.Item
import com.tsarsprocket.reportmid.lol.api.domain.model.RuneStyle
import com.tsarsprocket.reportmid.lol.api.domain.model.SummonerSpell

internal data class PlayerData(
    val championId: Long,
    val championLevel: Int,
    val gameName: String,
    val tagLine: String,
    val puuid: String,
    val summonerLevel: Int,
    val kills: Int,
    val deaths: Int,
    val assists: Int,
    val primaryRunes: RuneStyle,
    val secondaryRunes: RuneStyle,
    val summonerSpells: List<SummonerSpell>,
    val items: List<Item>,
    val ward: Item,
)
