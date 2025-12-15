package com.tsarsprocket.reportmid.matchData.api.data.model

import com.tsarsprocket.reportmid.lol.api.domain.model.Perk
import com.tsarsprocket.reportmid.lol.api.domain.model.RuneStyle

data class Participant(
    val assists: Int,
    val championId: Long,
    val championLevel: Int,
    val deaths: Int,
    val items: List<Int?>,
    val ward: Int?,
    val kills: Int,
    val primaryStyle: RuneStyle,
    val secondaryStyle: RuneStyle,
    val statPerks: List<Perk>,
    val profileIcon: Int,
    val puuid: String,
    val riotIdGameName: String,
    val riotIdTagline: String,
    val summonerLevel: Int,
    val summoner1Id: Int,
    val summoner2Id: Int,
    val teamPosition: String,
    val visionScore: Int,
)
