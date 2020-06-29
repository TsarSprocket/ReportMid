package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.core.championmastery.ChampionMastery
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

data class ChampionMasteryModel(
    val repository: Repository,
    val shadowMastery: ChampionMastery
) {

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
        champion = repository.getChampionModel( shadowMastery.champion )
        champion.load()
    }
}
