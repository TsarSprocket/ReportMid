package com.tsarsprocket.reportmid.riotapi.matchV4

import com.google.gson.annotations.SerializedName

data class TeamStatsDto(
    @SerializedName("towerKills") val towerKills: Int,
    @SerializedName("riftHeraldKills") val riftHeraldKills: Int,
    @SerializedName("firstBlood") val firstBlood: Boolean,
    @SerializedName("inhibitorKills") val inhibitorKills: Int,
    @SerializedName("bans") val bans: List<TeamBansDto>,
    @SerializedName("firstBaron") val firstBaron: Boolean,
    @SerializedName("firstDragon") val firstDragon: Boolean,
    @SerializedName("dominionVictoryScore") val dominionVictoryScore: Int,
    @SerializedName("dragonKills") val dragonKills: Int,
    @SerializedName("baronKills") val baronKills: Int,
    @SerializedName("firstInhibitor") val firstInhibitor: Boolean,
    @SerializedName("firstTower") val firstTower: Boolean,
    @SerializedName("vilemawKills") val vilemawKills: Int,
    @SerializedName("firstRiftHerald") val firstRiftHerald: Boolean,
    @SerializedName("teamId") val teamId: Int,
    @SerializedName("win") val win: String,
)