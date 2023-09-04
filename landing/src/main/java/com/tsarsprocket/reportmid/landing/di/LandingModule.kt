package com.tsarsprocket.reportmid.landing.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.base.di.FragmentKey
import com.tsarsprocket.reportmid.base.di.PerApi
import com.tsarsprocket.reportmid.base.di.ViewModelKey
import com.tsarsprocket.reportmid.base.viewmodel.ViewModelFactory
import com.tsarsprocket.reportmid.landing.usecase.LandingUseCase
import com.tsarsprocket.reportmid.landing.usecase.LandingUseCaseImpl
import com.tsarsprocket.reportmid.landing.view.LandingFragment
import com.tsarsprocket.reportmid.landing.viewmodel.LandingViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider

@Module
internal interface LandingModule {

    @Binds
    fun bindLandingUseCase(useCase: LandingUseCaseImpl): LandingUseCase

    @Binds
    @IntoMap
    @ViewModelKey(LandingViewModel::class)
    fun bindLandingViewModel(viewModel: LandingViewModel): ViewModel

    @Binds
    @IntoMap
    @FragmentKey(LandingFragment::class)
    fun bindLandingFragment(fragment: LandingFragment): Fragment

    companion object {

        @Provides
        @PerApi
        fun provideViewModelFactory(creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelFactory {
            return ViewModelFactory(creators)
        }
    }
}
