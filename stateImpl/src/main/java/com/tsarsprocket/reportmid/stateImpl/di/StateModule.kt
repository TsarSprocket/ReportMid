package com.tsarsprocket.reportmid.stateImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.stateApi.data.StateRepository
import com.tsarsprocket.reportmid.stateImpl.data.StateRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
internal interface StateModule {

    @Binds
    @PerApi
    fun bindStateRepository(repository: StateRepositoryImpl): StateRepository
}