package com.tsarsprocket.reportmid.di

import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.viewmodel.ManageMySummonersViewModel
import com.tsarsprocket.reportmid.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@ViewModelScope
@Subcomponent
interface ManageMySummonersViewModelComponent {

    fun inject(manageMySummonersViewModel: ManageMySummonersViewModel)

    @Subcomponent.Factory
    interface Factory {
        fun create(): ManageMySummonersViewModelComponent
    }
}

@Module(subcomponents = [ManageMySummonersViewModelComponent::class])
abstract class ManageMySummonersViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ManageMySummonersViewModel::class)
    abstract fun bindViewModel( manageMySummonersViewModel: ManageMySummonersViewModel ): ViewModel
}