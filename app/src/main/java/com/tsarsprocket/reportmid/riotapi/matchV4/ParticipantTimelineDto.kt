package com.tsarsprocket.reportmid.riotapi.matchV4

import com.google.gson.annotations.SerializedName

data class ParticipantTimelineDto(
    @SerializedName("participantId") val participantId: Int,
    @SerializedName("csDiffPerMinDeltas") val csDiffPerMinDeltas: Map<String,Double>,
    @SerializedName("damageTakenPerMinDeltas") val damageTakenPerMinDeltas: Map<String,Double>,
    @SerializedName("role") val role: String,
    @SerializedName("damageTakenDiffPerMinDeltas") val damageTakenDiffPerMinDeltas: Map<String,Double>,
    @SerializedName("xpPerMinDeltas") val xpPerMinDeltas: Map<String,Double>,
    @SerializedName("xpDiffPerMinDeltas") val xpDiffPerMinDeltas: Map<String,Double>,
    @SerializedName("lane") val lane: String,
    @SerializedName("creepsPerMinDeltas") val creepsPerMinDeltas: Map<String,Double>,
    @SerializedName("goldPerMinDeltas") val goldPerMinDeltas: Map<String,Double>,
)