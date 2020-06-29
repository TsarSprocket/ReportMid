package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.core.match.MatchHistory

class MatchHistoryModel(
    val repository: Repository,
    val shadowMatchHistory: MatchHistory
) {

    private val matches = MutableList<MatchModel?>( size ) { null }

    val size get() = shadowMatchHistory.size

    fun getMatch( i: Int ): MatchModel = matches[ i ] ?: repository.getMatchModel( shadowMatchHistory.get( i ) ).also { matches[ i ] = it }
}