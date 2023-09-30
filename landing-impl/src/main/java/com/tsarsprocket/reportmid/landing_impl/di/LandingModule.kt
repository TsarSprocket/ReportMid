package com.tsarsprocket.reportmid.landing_impl.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.base.di.FragmentKey
import com.tsarsprocket.reportmid.base.di.PerApi
import com.tsarsprocket.reportmid.base.di.ViewModelKey
import com.tsarsprocket.reportmid.base.viewmodel.ViewModelFactory
import com.tsarsprocket.reportmid.landing_api.view.LandingFragment
import com.tsarsprocket.reportmid.landing_impl.usecase.LandingUseCase
import com.tsarsprocket.reportmid.landing_impl.usecase.LandingUseCaseImpl
import com.tsarsprocket.reportmid.landing_impl.view.LandingFragmentImpl
import com.tsarsprocket.reportmid.landing_impl.viewmodel.LandingViewModel
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
    fun bindLandingFragment(fragment: LandingFragmentImpl): Fragment

    companion object {

        @Provides
        @PerApi
        fun provideViewModelFactory(creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelFactory {
            return ViewModelFactory(creators)
        }
    }
}
