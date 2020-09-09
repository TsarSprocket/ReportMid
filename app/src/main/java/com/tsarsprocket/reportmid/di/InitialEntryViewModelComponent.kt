package com.tsarsprocket.reportmid.di

import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.viewmodel.InitialEntryViewModel
import com.tsarsprocket.reportmid.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@ViewModelScope
@Subcomponent
interface InitialEntryViewModelComponent {

    fun inject(initialEntryViewModel: InitialEntryViewModel)

    @Subcomponent.Factory
    interface Factory {

        fun create(): InitialEntryViewModelComponent
    }
}

@Module( subcomponents = [ InitialEntryViewModelComponent::class ] )
abstract class InitialEntryViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey( InitialEntryViewModel::class )
    abstract fun bindViewModel( viewModel: InitialEntryViewModel): ViewModel
}
