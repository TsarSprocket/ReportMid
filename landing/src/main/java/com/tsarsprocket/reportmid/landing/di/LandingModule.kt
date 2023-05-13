package com.tsarsprocket.reportmid.landing.di

import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.base.di.ViewModelKey
import com.tsarsprocket.reportmid.landing.usecase.LandingUseCase
import com.tsarsprocket.reportmid.landing.usecase.LandingUseCaseImpl
import com.tsarsprocket.reportmid.landing.viewmodel.LandingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface LandingModule {

    @Binds
    fun bindLandingUseCase(useCase: LandingUseCaseImpl): LandingUseCase

    @Binds
    @IntoMap
    @ViewModelKey(LandingViewModel::class)
    fun bindLandingViewModel(viewModel: LandingViewModel): ViewModel
}