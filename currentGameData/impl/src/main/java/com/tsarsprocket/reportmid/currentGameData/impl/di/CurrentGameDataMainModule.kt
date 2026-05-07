package com.tsarsprocket.reportmid.currentGameData.impl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.currentGameData.api.data.CurrentGameDataRepository
import com.tsarsprocket.reportmid.currentGameData.impl.data.CurrentGameDataRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
internal interface CurrentGameDataMainModule {

    @Binds
    @PerApi
    fun bindCurrentGameDataRepository(repository: CurrentGameDataRepositoryImpl): CurrentGameDataRepository
}