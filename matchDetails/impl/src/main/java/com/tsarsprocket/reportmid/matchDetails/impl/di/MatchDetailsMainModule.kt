package com.tsarsprocket.reportmid.matchDetails.impl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.matchDetails.impl.domain.interactor.MatchDetailsInteractor
import com.tsarsprocket.reportmid.matchDetails.impl.domain.interactor.MatchDetailsInteractorImpl
import dagger.Binds
import dagger.Module

@Module
internal interface MatchDetailsMainModule {

    @Binds
    @PerApi
    fun bindMatchDetailsInteractor(interactor: MatchDetailsInteractorImpl): MatchDetailsInteractor
}