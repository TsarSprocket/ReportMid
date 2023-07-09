package com.tsarsprocket.reportmid.di

import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.base.di.ViewModelKey
import com.tsarsprocket.reportmid.overview.viewmodel.ProfileOverviewViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@ViewModelScope
@Subcomponent
interface ProfileOverviewViewModelComponent {
    fun inject(profileOverviewViewModel: ProfileOverviewViewModel)

    @Subcomponent.Factory
    interface Factory {
        fun create(): ProfileOverviewViewModelComponent
    }
}

@Module(subcomponents = [ProfileOverviewViewModelComponent::class])
abstract class ProfileOverviewViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProfileOverviewViewModel::class)
    abstract fun bindViewModel(profileOverviewViewModel: ProfileOverviewViewModel): ViewModel
}
