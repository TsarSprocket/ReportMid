package com.tsarsprocket.reportmid.di

import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.viewmodel.DrawerViewModel
import com.tsarsprocket.reportmid.base.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@ViewModelScope
@Subcomponent
interface DrawerViewModelComponent {
    fun inject(drawerViewModel: DrawerViewModel)

    @Subcomponent.Factory
    interface Factory {
        fun create(): DrawerViewModelComponent
    }
}

@Module(subcomponents = [DrawerViewModelComponent::class])
abstract class DrawerViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(DrawerViewModel::class)
    abstract fun bindViewModel(drawerViewModel: DrawerViewModel): ViewModel
}