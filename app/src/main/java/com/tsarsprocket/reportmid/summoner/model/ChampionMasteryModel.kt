package com.tsarsprocket.reportmid.summoner.model

import com.merakianalytics.orianna.Orianna
import com.merakianalytics.orianna.types.core.championmastery.ChampionMastery
import com.tsarsprocket.reportmid.model.ChampionModel
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.riotapi.championMastery.ChampionMasteryDto
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.Observable

class ChampionMasteryModel @AssistedInject constructor(
    @Assisted val championMasteryDto: ChampionMasteryDto,
    val repository: Repository,
) {
    val champion: Observable<ChampionModel> by lazy { repository.getChampionModel { Orianna.championWithId( championMasteryDto.championId.toInt() ).get() } }
    val level: Int = championMasteryDto.championLevel
    val points: Int = championMasteryDto.championPoints
}