package com.tsarsprocket.reportmid.matchHistory.impl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.matchHistory.impl.domain.interactor.MatchHistoryInteractor
import com.tsarsprocket.reportmid.matchHistory.impl.domain.interactor.MatchHistoryInteractorImpl
import dagger.Binds
import dagger.Module

@Module
internal interface MatchHistoryMainModule {

    @Binds
    @PerApi
    fun bindMatchHistoryInteractor(interactor: MatchHistoryInteractorImpl): MatchHistoryInteractor
}