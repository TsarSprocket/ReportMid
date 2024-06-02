package com.tsarsprocket.reportmid.di

import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.baseApi.di.ViewModelKey
import com.tsarsprocket.reportmid.viewmodel.MainActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@ViewModelScope
@Subcomponent
interface MainActivityViewModelComponent {

    fun inject(mainActivityViewModel: MainActivityViewModel)

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainActivityViewModelComponent
    }
}

@Module(subcomponents = [MainActivityViewModelComponent::class])
abstract class MainActivityViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel
}