package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.common.RunePath
import com.merakianalytics.orianna.types.core.match.Participant
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class ParticipantModel( private val repository: Repository, val team: TeamModel, private val shadowParticipant: Participant ) {
    val summoner by lazy { repository.getSummonerModel( shadowParticipant.summoner ).replay( 1 ).autoConnect() }
    val champion by lazy { repository.getChampionModel( shadowParticipant.champion ).replay( 1 ).autoConnect() }
    val kills = shadowParticipant.stats.kills
    val deaths = shadowParticipant.stats.deaths
    val assists = shadowParticipant.stats.assists
    val isWinner = shadowParticipant.stats.isWinner
    val creepScore = shadowParticipant.stats.creepScore
    val summonerSpellD by lazy { Observable.fromCallable { shadowParticipant.summonerSpellD!! }.subscribeOn( Schedulers.io() ).flatMap { spell -> repository.getSummonerSpell { spell } }.replay( 1 ).autoConnect() }
    val summonerSpellF by lazy { Observable.fromCallable { shadowParticipant.summonerSpellF!! }.subscribeOn( Schedulers.io() ).flatMap { spell -> repository.getSummonerSpell { spell } }.replay( 1 ).autoConnect() }
    val items by lazy{ getObservableItemsList().replay( 1 ).autoConnect() }
    val primaryRunePath by lazy { getMaybePath( shadowParticipant.primaryRunePath ).replay( 1 ).autoConnect() }
    val secondaryRunePath by lazy { getMaybePath( shadowParticipant.secondaryRunePath ).replay( 1 ).autoConnect() }
    val runeStats by lazy { getObservableRuneStatsList().replay( 1 ).autoConnect() }

    private fun getObservableRuneStatsList() = Observable.fromCallable {
        List( shadowParticipant.runeStats.size ) { i -> repository.getRuneStats( shadowParticipant.runeStats[ i ] ).replay().autoConnect() }
    }.subscribeOn( Schedulers.io() )

    private fun getObservableItemsList(): Observable<List<Observable<ItemModel>>> = Observable.fromCallable {
        List( shadowParticipant.items.size ) { i ->
            repository.getItemModel( shadowParticipant.items[ i ] ).replay( 1 ).autoConnect()
        }
    }.observeOn( Schedulers.io() )

    private fun getMaybePath( runePath: RunePath? ): Observable<Maybe<RunePathModel>> {
        return Observable.fromCallable {
            val path = runePath
            if (path != null) Maybe.just(path) else Maybe.empty()
        }.subscribeOn(Schedulers.io()).map { path ->
            Repository.getRunePath(if (path.isEmpty.blockingGet()) Maybe.empty() else Maybe.just(path.blockingGet().id))
        }
    }
}