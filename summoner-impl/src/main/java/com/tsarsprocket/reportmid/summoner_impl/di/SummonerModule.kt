package com.tsarsprocket.reportmid.summoner_impl.di

import com.tsarsprocket.reportmid.base.di.PerApi
import com.tsarsprocket.reportmid.summoner_api.data.SummonerRepository
import com.tsarsprocket.reportmid.summoner_impl.data.SummonerRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface SummonerModule {

    @Binds
    @PerApi
    fun bindSummonerRepository(summonerRepository: SummonerRepositoryImpl): SummonerRepository
}