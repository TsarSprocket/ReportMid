package com.tsarsprocket.reportmid.matchData.api.data.model

import com.tsarsprocket.reportmid.lol.api.model.Perk

data class Participant(
    val assists: Int,
    val championId: Int,
    val deaths: Int,
    val item0: Int,
    val item1: Int,
    val item2: Int,
    val item3: Int,
    val item4: Int,
    val item5: Int,
    val kills: Int,
    val primaryStyle: RuneStyle,
    val secondaryStyle: RuneStyle,
    val statPerks: List<Perk>,
    val profileIcon: Int,
    val puuid: String,
    val riotIdGameName: String,
    val riotIdTagline: String,
    val summoner1Id: Int,
    val summoner2Id: Int,
    val teamPosition: String,
    val visionScore: Int,
)
