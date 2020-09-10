package com.tsarsprocket.reportmid.di

import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.viewmodel.AddSummonerViewModel
import com.tsarsprocket.reportmid.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@ViewModelScope
@Subcomponent
interface AddSummonerViewModelComponent {

    fun inject(addSummonerViewModel: AddSummonerViewModel)

    @Subcomponent.Factory
    interface Factory {

        fun create(): AddSummonerViewModelComponent
    }
}

@Module( subcomponents = [ AddSummonerViewModelComponent::class ] )
abstract class AddSummonerViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey( AddSummonerViewModel::class )
    abstract fun bindViewModel( viewModel: AddSummonerViewModel): ViewModel
}
