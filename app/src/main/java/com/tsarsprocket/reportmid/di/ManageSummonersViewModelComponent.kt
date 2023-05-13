package com.tsarsprocket.reportmid.di

import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.viewmodel.ManageSummonersViewModel
import com.tsarsprocket.reportmid.base.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@ViewModelScope
@Subcomponent
interface ManageSummonersViewModelComponent {

    fun inject(manageSummonersViewModel: ManageSummonersViewModel)

    @Subcomponent.Factory
    interface Factory {
        fun create(): ManageSummonersViewModelComponent
    }
}

@Module(subcomponents = [ManageSummonersViewModelComponent::class])
abstract class ManageSummonersViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ManageSummonersViewModel::class)
    abstract fun bindViewModel(manageSummonersViewModel: ManageSummonersViewModel): ViewModel
}