package com.tsarsprocket.reportmid.matchUpView.impl.domain.model


internal data class Team(
    val participants: List<Participant>,
    val bannedChampionIds: List<Int>,
)
