package com.tsarsprocket.reportmid.appBar.impl.di

import com.tsarsprocket.reportmid.appBar.api.navigation.AppBarInitialViewState
import com.tsarsprocket.reportmid.appBar.impl.viewState.AppBarState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import dagger.Module
import dagger.Provides
import kotlinx.collections.immutable.persistentListOf

@Module
internal class MainModule {

    @Provides
    @AppBarInitialViewState
    fun provideAppBarInitialViewState(): ViewState = AppBarState(persistentListOf())
}
