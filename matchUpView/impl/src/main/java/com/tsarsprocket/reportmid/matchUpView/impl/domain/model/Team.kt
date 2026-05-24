package com.tsarsprocket.reportmid.matchUpView.impl.domain.model


internal data class Team(
    val id: Int,
    val participants: List<Participant>,
    val bannedChampionIds: List<Int>,
)
