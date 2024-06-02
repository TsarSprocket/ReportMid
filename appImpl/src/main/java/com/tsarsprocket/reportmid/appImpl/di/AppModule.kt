package com.tsarsprocket.reportmid.appImpl.di

import android.content.Context
import com.tsarsprocket.reportmid.appApi.di.AppContext
import com.tsarsprocket.reportmid.appApi.room.MainStorage
import com.tsarsprocket.reportmid.appImpl.application.ReportMidApplication
import com.tsarsprocket.reportmid.baseApi.di.AppScope
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Computation
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Io
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Ui
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
internal interface AppModule {

    companion object {

        @Provides
        @AppScope
        @Ui
        fun provideUiDespatcher(): CoroutineDispatcher = Dispatchers.Main

        @Provides
        @AppScope
        @Io
        fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

        @Provides
        @AppScope
        @Computation
        fun provideComputationDispatcher(): CoroutineDispatcher = Dispatchers.Default

        @Provides
        @AppScope
        fun provideMainStorage(): MainStorage = TODO()

        @Provides
        @AppScope
        @AppContext
        fun provideApplicationContext(): Context = ReportMidApplication.theInstance.applicationContext
    }
}