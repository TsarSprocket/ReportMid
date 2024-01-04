package com.tsarsprocket.reportmid.app.di

import android.content.Context
import com.tsarsprocket.reportmid.ReportMidApp
import com.tsarsprocket.reportmid.app_api.room.MainStorage
import com.tsarsprocket.reportmid.base.di.PerApi
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
    @PerApi
    fun provideAppContext(): Context = ReportMidApp.instance

    // Schedulers

    @Provides
    @Computation
    @PerApi
    fun provideComputationDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Io
    @PerApi
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Ui
    @PerApi
    fun provideUiDispatcher(): CoroutineDispatcher = Dispatchers.Main

    // Storage

    @Provides
    @PerApi
    fun provideMainStorage(): MainStorage = ReportMidApp.instance.comp.getMainStorage()
}
