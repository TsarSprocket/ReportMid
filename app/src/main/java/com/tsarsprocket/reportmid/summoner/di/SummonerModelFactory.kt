package com.tsarsprocket.reportmid.summoner.di

import com.tsarsprocket.reportmid.model.RegionModel
import com.tsarsprocket.reportmid.riotapi.summoner.SummonerDto
import com.tsarsprocket.reportmid.summoner.model.ChampionMasteryModel
import com.tsarsprocket.reportmid.summoner.model.SummonerModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import io.reactivex.Single

@AssistedFactory
interface SummonerModelFactory {
    fun create( region: RegionModel, summonerDto: SummonerDto, masteries: Single<List<ChampionMasteryModel>>): SummonerModel
}