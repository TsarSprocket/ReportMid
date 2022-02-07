package com.tsarsprocket.reportmid.riotapi.matchV5

import com.google.gson.annotations.SerializedName

data class ParticipantIdentityDto(
    @SerializedName("participantId") val participantId: Int,
    @SerializedName("player") val player: PlayerDto,
)