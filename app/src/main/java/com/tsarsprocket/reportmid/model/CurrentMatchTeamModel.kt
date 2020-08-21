package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.core.spectator.CurrentMatchTeam
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject

class CurrentMatchTeamModel( private val repository: Repository, shadowCurrentMatchTeam : CurrentMatchTeam ) {
    val participants by lazy { ReplaySubject.createWithSize<List<ReplaySubject<PlayerModel>>>( 1 ).also { Observable.fromCallable { shadowCurrentMatchTeam.participants }
        .observeOn( Schedulers.io() )
        .map { list -> List( list.size ) { i -> repository.getPlayer { list[ i ] } } }.subscribe( it ) } }
}