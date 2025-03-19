package com.tsarsprocket.reportmid.viewStateImpl.di

import android.os.Bundle
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.baseApi.di.ViewModelKey
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.DefaultState
import com.tsarsprocket.reportmid.baseApi.viewmodel.ConvenienceViewModelFactoryCreator
import com.tsarsprocket.reportmid.baseApi.viewmodel.ViewModelFactoryCreator
import com.tsarsprocket.reportmid.viewStateApi.viewState.EmptyScreenViewState
import com.tsarsprocket.reportmid.viewStateImpl.backstack.BackStack
import com.tsarsprocket.reportmid.viewStateImpl.viewmodel.ViewStateHolderImpl
import com.tsarsprocket.reportmid.viewStateImpl.viewmodel.ViewStateViewModel
import com.tsarsprocket.reportmid.viewStateImpl.viewmodel.ViewStateViewModel.Companion.KEY_BACKSTACK
import com.tsarsprocket.reportmid.viewStateImpl.viewmodel.ViewStateViewModel.Companion.KEY_ROOT_HOLDER
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
internal class ViewStateViewModelModule {

    @Provides
    @PerApi
    @IntoMap
    @ViewModelKey(ViewStateViewModel::class)
    fun provideViewModelFactoryCreator(
        viewModelFactory: ViewStateViewModel.Factory,
        @DefaultState(ViewStateViewModel::class) defaultState: Bundle,
    ): ViewModelFactoryCreator = ConvenienceViewModelFactoryCreator(defaultState) { handle ->
        viewModelFactory.create(handle)
    }

    @Provides
    @DefaultState(ViewStateViewModel::class)
    fun provideViewStateViewModelDefaultState(): Bundle {
        return Bundle(2).apply {
            putParcelable(KEY_ROOT_HOLDER, ViewStateHolderImpl(EmptyScreenViewState))
            putParcelable(KEY_BACKSTACK, BackStack())
        }
    }
}