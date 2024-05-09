package com.tsarsprocket.reportmid.di

import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.base_api.di.ViewModelKey
import com.tsarsprocket.reportmid.viewmodel.MatchHistoryViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@ViewModelScope
@Subcomponent
interface MatchHistoryViewModelComponent {

    fun inject(matchHistoryViewModel: MatchHistoryViewModel)

    @Subcomponent.Factory
    interface Factory {
        fun create(): MatchHistoryViewModelComponent
    }
}

@Module(subcomponents = [MatchHistoryViewModelComponent::class])
abstract class MatchHistoryViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MatchHistoryViewModel::class)
    abstract fun bindViewModel(matchHistoryViewModel: MatchHistoryViewModel): ViewModel
}
