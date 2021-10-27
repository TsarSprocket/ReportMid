package com.tsarsprocket.reportmid.summoner.di

import com.tsarsprocket.reportmid.summoner.model.SummonerRepository
import com.tsarsprocket.reportmid.summoner.model.SummonerRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface SummonerModule {

    @Binds
    fun bindSummonerRepository(summonerRepository: SummonerRepositoryImpl): SummonerRepository
}