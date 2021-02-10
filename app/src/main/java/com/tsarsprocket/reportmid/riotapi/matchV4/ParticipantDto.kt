package com.tsarsprocket.reportmid.riotapi.matchV4

import com.google.gson.annotations.SerializedName

data class ParticipantDto(
    @SerializedName("participantId") val participantId: Int,
    @SerializedName("championId") val championId: Int,
    @SerializedName("runes") val runes: List<RuneDto>,
    @SerializedName("stats") val stats: ParticipantStatsDto,
    @SerializedName("teamId") val teamId: Int, // 100 - blue, 200 - red
    @SerializedName("timeline") val timeline: ParticipantTimelineDto,
    @SerializedName("spell1Id") val spell1Id: Int,
    @SerializedName("spell2Id") val spell2Id: Int,
    @SerializedName("highestAchievedSeasonTier") val highestAchievedSeasonTier: String,
    @SerializedName("masteries") val masteries: List<MasteryDto>
)