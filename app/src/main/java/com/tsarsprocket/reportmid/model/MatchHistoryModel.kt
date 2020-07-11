package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.core.match.MatchHistory
import io.reactivex.Observable

class MatchHistoryModel(
    val repository: Repository,
    val shadowMatchHistory: MatchHistory
) {

    private val matches = MutableList<Observable<MatchModel>?>( size ) { null }

    val size get() = shadowMatchHistory.size

    fun getMatch( i: Int ): Observable<MatchModel> =
        matches[ i ]?: repository.getMatchModel( shadowMatchHistory[ i ] ).replay( 1 ).autoConnect().also { matches[ i ] = it }
}