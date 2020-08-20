package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.core.spectator.CurrentMatchTeam
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class CurrentMatchTeamModel( private val repository: Repository, shadowCurrentMatchTeam : CurrentMatchTeam ) {
    val participants by lazy { BehaviorSubject.create<List<BehaviorSubject<PlayerModel>>>().also { Observable.fromCallable { shadowCurrentMatchTeam.participants }
        .observeOn( Schedulers.io() )
        .map { list -> List( list.size ) { i -> repository.getPlayer { list[ i ] } } }.subscribe( it ) } }
}