package com.tsarsprocket.reportmid.di

import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.baseApi.di.ViewModelKey
import com.tsarsprocket.reportmid.viewmodel.MatchupViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@ViewModelScope
@Subcomponent
interface MatchupViewModelComponent {

    fun inject( matchupViewModel: MatchupViewModel)

    @Subcomponent.Factory
    interface Factory {

        fun create(): MatchupViewModelComponent
    }
}

@Module( subcomponents = [ MatchupViewModelComponent::class ] )
abstract class MatchupViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MatchupViewModel::class)
    abstract fun bindViewModel( matchupViewModel: MatchupViewModel): ViewModel
}