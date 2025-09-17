package com.tsarsprocket.reportmid.matchHistory.impl.domain.model

internal data class MatchesData(
    val firstPosition: Int,
    val matches: List<MatchData>,
    val hasMoreToLoad: Boolean,
)
