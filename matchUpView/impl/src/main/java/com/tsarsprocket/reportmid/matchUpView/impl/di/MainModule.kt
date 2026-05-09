package com.tsarsprocket.reportmid.matchUpView.impl.di

import com.tsarsprocket.reportmid.matchUpView.impl.domain.Interactor
import com.tsarsprocket.reportmid.matchUpView.impl.domain.InteractorImpl
import dagger.Binds
import dagger.Module

@Module
internal interface MainModule {

    @Binds
    fun bind(interactor: InteractorImpl): Interactor
}
