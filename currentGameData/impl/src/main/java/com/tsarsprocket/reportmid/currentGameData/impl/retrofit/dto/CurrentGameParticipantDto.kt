package com.tsarsprocket.reportmid.currentGameData.impl.retrofit.dto

import com.google.gson.annotations.SerializedName

internal data class CurrentGameParticipantDto(
    @SerializedName("championId") val championId: Long,
    @SerializedName("perks") val perks: PerksDto,
    @SerializedName("profileIconId") val profileIconId: Int,
    @SerializedName("bot") val bot: Boolean,
    @SerializedName("teamId") val teamId: Int,
    @SerializedName("puuid") val puuid: String?,
    @SerializedName("spell1Id") val spell1Id: Long,
    @SerializedName("spell2Id") val spell2Id: Long,
)
