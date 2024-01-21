package com.tsarsprocket.reportmid.summoner.di

import com.tsarsprocket.reportmid.summoner_impl.data.SummonerRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface SummonerModule {

    @Binds
    fun bindSummonerRepository(summonerRepository: SummonerRepositoryImpl): com.tsarsprocket.reportmid.summoner_api.data.SummonerRepository
}