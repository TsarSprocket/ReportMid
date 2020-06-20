package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.core.match.MatchHistory

class MatchHistoryModel(
    val shadowMatchHistory: MatchHistory
) {

    private val matches = MutableList<MatchModel?>( size ) { null }

    val size get() = shadowMatchHistory.size

    fun getMatch( i: Int ): MatchModel = matches[ i ] ?: MatchModel( shadowMatchHistory.get( i ) ).also { matches[ i ] = it }
}