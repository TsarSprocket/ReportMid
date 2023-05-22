package com.tsarsprocket.reportmid.landing.di

import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.base.capability.PerCapability
import com.tsarsprocket.reportmid.base.di.ViewModelKey
import com.tsarsprocket.reportmid.base.viewmodel.ViewModelFactory
import com.tsarsprocket.reportmid.landing.usecase.LandingUseCase
import com.tsarsprocket.reportmid.landing.usecase.LandingUseCaseImpl
import com.tsarsprocket.reportmid.landing.viewmodel.LandingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Provider

@Module
interface LandingModule {

    @Binds
    fun bindLandingUseCase(useCase: LandingUseCaseImpl): LandingUseCase

    @Binds
    @IntoMap
    @ViewModelKey(LandingViewModel::class)
    fun bindLandingViewModel(viewModel: LandingViewModel): ViewModel

    companion object {

        @PerCapability
        fun provideViewModelFactory(creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelFactory {
            return ViewModelFactory(creators)
        }
    }
}