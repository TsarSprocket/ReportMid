package com.tsarsprocket.reportmid.findSummonerImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.findSummonerApi.viewIntent.FindSummonerViewIntent
import com.tsarsprocket.reportmid.findSummonerImpl.domain.FindSummonerUseCase
import com.tsarsprocket.reportmid.findSummonerImpl.domain.FindSummonerUseCaseImpl
import com.tsarsprocket.reportmid.findSummonerImpl.viewIntent.FindSummonerViewIntentImpl
import dagger.Binds
import dagger.Module

@Module
internal interface FindSummonerModule {

    @Binds
    @PerApi
    fun bindViewIntentProducer(producer: FindSummonerViewIntentImpl.Factory): FindSummonerViewIntent.Factory

    @Binds
    @PerApi
    fun bindUseCase(useCase: FindSummonerUseCaseImpl): FindSummonerUseCase
}