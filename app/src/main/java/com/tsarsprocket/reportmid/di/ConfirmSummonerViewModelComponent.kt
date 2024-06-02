package com.tsarsprocket.reportmid.di

import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.baseApi.di.ViewModelKey
import com.tsarsprocket.reportmid.viewmodel.ConfirmSummonerViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@ViewModelScope
@Subcomponent
interface ConfirmSummonerViewModelComponent {
    fun inject(confirmSummonerViewModel: ConfirmSummonerViewModel)

    @Subcomponent.Factory
    interface Factory {
        fun create(): ConfirmSummonerViewModelComponent
    }
}

@Module(subcomponents = [ConfirmSummonerViewModelComponent::class])
abstract class ConfirmSummonerViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ConfirmSummonerViewModel::class)
    abstract fun bindViewModel(confirmSummonerViewModel: ConfirmSummonerViewModel): ViewModel
}