package com.tsarsprocket.reportmid.model

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

data class ChampionMasteryModel( val shadowMastery: com.merakianalytics.orianna.types.core.championmastery.ChampionMastery ) {

    lateinit var champion: ChampionModel
    val level = shadowMastery.level
    val points = shadowMastery.points

    fun loadAsync(): Single<ChampionMasteryModel> {
        return Single.fromCallable {
            load()
            return@fromCallable this
        }.subscribeOn( Schedulers.io() )
    }

    fun load() {
        champion = ChampionModel( shadowMastery.champion )
        champion.load()
    }
}
