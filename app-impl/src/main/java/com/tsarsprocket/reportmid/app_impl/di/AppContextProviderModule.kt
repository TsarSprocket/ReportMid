package com.tsarsprocket.reportmid.app_impl.di

import android.content.Context
import com.tsarsprocket.reportmid.app_api.di.AppContext
import com.tsarsprocket.reportmid.base.di.AppScope
import dagger.Module
import dagger.Provides

@Module
internal class AppContextProviderModule(
    private val appContextProvider: () -> Context,
) {

    @Provides
    @AppScope
    @AppContext
    fun provideApplicationContext(): Context = appContextProvider()
}