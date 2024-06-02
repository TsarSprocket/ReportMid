package com.tsarsprocket.reportmid.summonerImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.summonerApi.data.SummonerRepository
import com.tsarsprocket.reportmid.summonerImpl.data.SummonerRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface SummonerModule {

    @Binds
    @PerApi
    fun bindSummonerRepository(summonerRepository: SummonerRepositoryImpl): SummonerRepository
}