package com.tsarsprocket.reportmid.profileOverviewImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.profileOverviewImpl.domain.ProfileOverviewUseCase
import com.tsarsprocket.reportmid.profileOverviewImpl.domain.ProfileOverviewUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
internal interface ProfileOverviewMainModule {

    @Binds
    @PerApi
    fun bindProfileOverviewUseCase(useCase: ProfileOverviewUseCaseImpl): ProfileOverviewUseCase
}