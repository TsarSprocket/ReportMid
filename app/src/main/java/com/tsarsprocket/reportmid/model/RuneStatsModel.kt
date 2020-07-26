package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.core.match.RuneStats
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class RuneStatsModel( val repository: Repository, private val shadowRuneStats: RuneStats ) {
    val rune by lazy { getTheRune().replay( 1 ).autoConnect() }

    private fun getTheRune() =
        Observable.fromCallable { shadowRuneStats.rune!! }.subscribeOn( Schedulers.io() ).flatMap { reforgedRune -> repository.getRune(reforgedRune) }
}