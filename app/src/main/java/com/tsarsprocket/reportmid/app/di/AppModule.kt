package com.tsarsprocket.reportmid.app.di

import android.content.Context
import com.tsarsprocket.reportmid.ReportMidApp
import com.tsarsprocket.reportmid.app_api.request_manager.RequestManager
import com.tsarsprocket.reportmid.app_api.room.MainStorage
import com.tsarsprocket.reportmid.base.di.PerApi
import com.tsarsprocket.reportmid.base.di.qualifiers.Computation
import com.tsarsprocket.reportmid.base.di.qualifiers.Io
import com.tsarsprocket.reportmid.base.di.qualifiers.Ui
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher

@Module
internal class AppModule {

    @Provides
    @PerApi
    fun provideAppContext(): Context = ReportMidApp.instance

    // Schedulers

    @Provides
    @Computation
    @PerApi
    fun provideComputationDispatcher(): CoroutineDispatcher = ReportMidApp.instance.comp.getComputationDispatcher()

    @Provides
    @Io
    @PerApi
    fun provideIoDispatcher(): CoroutineDispatcher = ReportMidApp.instance.comp.getIoDispatcher()

    @Provides
    @Ui
    @PerApi
    fun provideUiDispatcher(): CoroutineDispatcher = ReportMidApp.instance.comp.getUiDispatcher()

    // Storage

    @Provides
    @PerApi
    fun provideMainStorage(): MainStorage = ReportMidApp.instance.comp.getMainStorage()

    @Provides
    @PerApi
    fun provideRequestManager(): RequestManager = ReportMidApp.instance.comp.getRequestManager()
}
