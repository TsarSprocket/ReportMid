package com.tsarsprocket.reportmid.di

import com.tsarsprocket.reportmid.LandingViewModel
import dagger.Module
import dagger.Subcomponent

@ViewModelScope
@Subcomponent
interface LandingViewModelComponent {

    fun inject( landingViewModel: LandingViewModel )

    @Subcomponent.Factory
    interface Factory {

        fun create(): LandingViewModelComponent
    }
}

@Module( subcomponents = [ LandingViewModelComponent::class ] )
class LandingViewModelModule {}
