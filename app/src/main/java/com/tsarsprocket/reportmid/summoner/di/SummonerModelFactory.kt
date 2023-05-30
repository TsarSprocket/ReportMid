package com.tsarsprocket.reportmid.summoner.di

import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.riotapi.summoner.SummonerDto
import com.tsarsprocket.reportmid.summoner.model.ChampionMasteryModel
import com.tsarsprocket.reportmid.summoner.model.SummonerModel
import dagger.assisted.AssistedFactory
import io.reactivex.Single

@AssistedFactory
interface SummonerModelFactory {
    fun create(region: Region, summonerDto: SummonerDto, masteries: Single<List<ChampionMasteryModel>>): SummonerModel
}