package com.tsarsprocket.reportmid.di

import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.baseApi.di.ViewModelKey
import com.tsarsprocket.reportmid.viewmodel.ManageFriendsViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@ViewModelScope
@Subcomponent
interface ManageFriendsViewModelComponent {

    fun inject(manageFriendsViewModel: ManageFriendsViewModel)

    @Subcomponent.Factory
    interface Factory {
        fun create(): ManageFriendsViewModelComponent
    }
}

@Module(subcomponents = [ManageFriendsViewModelComponent::class])
abstract class ManageFriendsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ManageFriendsViewModel::class)
    abstract fun bindViewModel(manageFriendsViewModel: ManageFriendsViewModel): ViewModel
}
