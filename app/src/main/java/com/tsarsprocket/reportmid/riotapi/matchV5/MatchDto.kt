package com.tsarsprocket.reportmid.riotapi.matchV5

import com.google.gson.annotations.SerializedName

data class MatchDto(
    @SerializedName("metadata") val metadata: MetadataDto,
    @SerializedName("info") val info: InfoDto,
)

/*
@SerializedName("gameId") val gameId: Long,
@SerializedName("participantIdentities") val participantIdentities: List<ParticipantIdentityDto>,
@SerializedName("teams") val teams: List<TeamStatsDto>,
@SerializedName("platformId") val platformId: String,
@SerializedName("gameCreation") val gameCreation: Long,
@SerializedName("seasonId") val seasonId: Int,
@SerializedName("gameVersion") val gameVersion: String,
@SerializedName("participants") val participants: List<ParticipantDto>,
*/
