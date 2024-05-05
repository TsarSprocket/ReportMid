package com.tsarsprocket.reportmid.app_impl.di

import com.tsarsprocket.reportmid.app_api.room.MainStorage
import com.tsarsprocket.reportmid.base.di.AppScope
import com.tsarsprocket.reportmid.base.di.qualifiers.Computation
import com.tsarsprocket.reportmid.base.di.qualifiers.Io
import com.tsarsprocket.reportmid.base.di.qualifiers.Ui
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
    }
}