package com.tsarsprocket.reportmid.summoner.di

import com.tsarsprocket.reportmid.riotapi.championMastery.ChampionMasteryDto
import com.tsarsprocket.reportmid.summoner.model.ChampionMasteryModel
import dagger.assisted.AssistedFactory

@AssistedFactory
interface ChampionMasteryModelFactory {
    fun create( championMasteryDto: ChampionMasteryDto): ChampionMasteryModel
}