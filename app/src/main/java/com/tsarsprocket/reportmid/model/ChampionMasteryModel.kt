package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.core.championmastery.ChampionMastery
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

data class ChampionMasteryModel(
    val repository: Repository,
    val shadowMastery: ChampionMastery
) {
    val champion: Observable<ChampionModel> by lazy { repository.getChampionModel { shadowMastery.champion } }
    val level = shadowMastery.level
    val points = shadowMastery.points
}
