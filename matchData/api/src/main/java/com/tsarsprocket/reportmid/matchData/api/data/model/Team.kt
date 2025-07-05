package com.tsarsprocket.reportmid.matchData.api.data.model

data class Team(
    var participants: List<Participant>,
    val isWinner: Boolean,
)
