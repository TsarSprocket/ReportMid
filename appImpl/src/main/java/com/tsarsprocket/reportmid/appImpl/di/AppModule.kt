package com.tsarsprocket.reportmid.appImpl.di

import android.content.Context
import androidx.room.Room
import com.tsarsprocket.reportmid.appApi.di.AppContext
import com.tsarsprocket.reportmid.appApi.room.MainStorage
import com.tsarsprocket.reportmid.appImpl.application.ReportMidApplication
import com.tsarsprocket.reportmid.appImpl.room.MainDatabase
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

        private const val MAIN_DATABASE_NAME = "report_mid_database"

        @Provides
        @AppScope
        @Ui
        fun provideUiDispatcher(): CoroutineDispatcher = Dispatchers.Main

        @Provides
        @AppScope
        @Ui.Immediate
        fun provideImmediateUiDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate

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
        fun provideMainStorage(
            @AppContext
            context: Context,
        ): MainStorage {
            return Room.databaseBuilder(context.applicationContext, MainDatabase::class.java, MAIN_DATABASE_NAME)
                .build()
        }

        @Provides
        @AppScope
        @AppContext
        fun provideApplicationContext(): Context = ReportMidApplication.theInstance.applicationContext
    }
}