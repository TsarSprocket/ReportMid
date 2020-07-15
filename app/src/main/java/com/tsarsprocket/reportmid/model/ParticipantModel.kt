package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.core.match.Participant
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class ParticipantModel( private val repository: Repository, val team: TeamModel, private val shadowParticipant: Participant ) {
    val summoner by lazy{ repository.getSummonerModel( shadowParticipant.summoner ).replay( 1 ).autoConnect() }
    val champion by lazy{ repository.getChampionModel( shadowParticipant.champion ).replay( 1 ).autoConnect() }
    val kills = shadowParticipant.stats.kills
    val deaths = shadowParticipant.stats.deaths
    val assists = shadowParticipant.stats.assists
    val isWinner = shadowParticipant.stats.isWinner
    val creepScore = shadowParticipant.stats.creepScore
    val items by lazy{ getObservableItemsList().replay( 1 ).autoConnect() }

    private fun getObservableItemsList(): Observable<List<Observable<ItemModel>>> = Observable.fromCallable {
        List( shadowParticipant.items.size ) { i ->
            repository.getItemModel( shadowParticipant.items[ i ] ).replay( 1 ).autoConnect()
        }
    }.observeOn( Schedulers.io() )
}