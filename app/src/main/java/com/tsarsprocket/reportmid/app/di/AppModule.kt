package com.tsarsprocket.reportmid.app.di

import android.content.Context
import com.tsarsprocket.reportmid.ReportMidApp
import com.tsarsprocket.reportmid.base.di.qualifiers.Computation
import com.tsarsprocket.reportmid.base.di.qualifiers.Io
import com.tsarsprocket.reportmid.base.di.qualifiers.Ui
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
internal class AppModule {

    @Provides
    fun provideAppContext(): Context = ReportMidApp.instance

    @Provides
    @Computation
    fun provideComputationDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Io
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Ui
    fun provideUiDispatcher(): CoroutineDispatcher = Dispatchers.Main
}
