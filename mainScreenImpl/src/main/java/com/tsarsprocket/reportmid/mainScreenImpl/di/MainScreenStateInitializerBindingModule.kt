package com.tsarsprocket.reportmid.mainScreenImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.mainScreenImpl.stateInitializer.MainScreenStateInitializer
import com.tsarsprocket.reportmid.mainScreenImpl.viewState.MainScreenViewState
import com.tsarsprocket.reportmid.viewStateApi.di.ViewStateKey
import com.tsarsprocket.reportmid.viewStateApi.stateInitializer.ViewStateInitializer
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MainScreenStateInitializerBindingModule {

    @Binds
    @PerApi
    @IntoMap
    @ViewStateKey(MainScreenViewState::class)
    fun bindMainScreenStateInitializer(initializer: MainScreenStateInitializer): ViewStateInitializer
}