package com.tsarsprocket.reportmid.summoner.di

import com.tsarsprocket.reportmid.summoner_api.model.ChampionMasteryModel
import com.tsarsprocket.reportmid.summoner_impl.retrofit.championMastery.ChampionMasteryDto
import dagger.assisted.AssistedFactory

@AssistedFactory
interface ChampionMasteryModelFactory {
    fun create( championMasteryDto: ChampionMasteryDto): ChampionMasteryModel
}