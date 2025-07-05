package com.tsarsprocket.reportmid.matchData.impl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.matchData.api.data.MatchDataRepository
import com.tsarsprocket.reportmid.matchData.impl.data.MatchDataRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
internal interface MatchDataModule {

    @Binds
    @PerApi
    fun bindMatchDataRepository(repository: MatchDataRepositoryImpl): MatchDataRepository
}