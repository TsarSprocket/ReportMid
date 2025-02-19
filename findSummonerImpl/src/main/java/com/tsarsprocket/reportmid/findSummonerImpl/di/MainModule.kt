package com.tsarsprocket.reportmid.findSummonerImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.findSummonerImpl.domain.FindSummonerUseCase
import com.tsarsprocket.reportmid.findSummonerImpl.domain.FindSummonerUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
internal interface MainModule {

    @Binds
    @PerApi
    fun bindUseCase(useCase: FindSummonerUseCaseImpl): FindSummonerUseCase
}