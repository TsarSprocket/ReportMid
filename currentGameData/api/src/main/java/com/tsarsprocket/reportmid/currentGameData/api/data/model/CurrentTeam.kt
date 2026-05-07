package com.tsarsprocket.reportmid.currentGameData.api.data.model

data class CurrentTeam(
    val teamId: Int,
    val participants: List<CurrentGameParticipant>,
    val bannedChampionIds: List<Int>,
)
