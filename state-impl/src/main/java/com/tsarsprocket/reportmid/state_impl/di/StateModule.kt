package com.tsarsprocket.reportmid.state_impl.di

import com.tsarsprocket.reportmid.base_api.di.PerApi
import com.tsarsprocket.reportmid.state_api.data.StateRepository
import com.tsarsprocket.reportmid.state_impl.data.StateRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
internal interface StateModule {

    @Binds
    @PerApi
    fun bindStateRepository(repository: StateRepositoryImpl): StateRepository
}