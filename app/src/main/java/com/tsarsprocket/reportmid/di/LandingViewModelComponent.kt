package com.tsarsprocket.reportmid.di

import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.base.di.ViewModelKey
import com.tsarsprocket.reportmid.viewmodel.LandingViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@ViewModelScope
@Subcomponent
interface LandingViewModelComponent {
    fun inject( landingViewModel: LandingViewModel)

    @Subcomponent.Factory
    interface Factory {
        fun create(): LandingViewModelComponent
    }
}

@Module( subcomponents = [ LandingViewModelComponent::class ] )
abstract class LandingViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey( LandingViewModel::class )
    abstract fun bindViewModel( landingViewModel: LandingViewModel): ViewModel
}