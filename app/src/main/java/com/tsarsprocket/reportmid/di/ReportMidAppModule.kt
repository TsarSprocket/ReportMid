package com.tsarsprocket.reportmid.di

import android.content.Context
import androidx.room.Room
import com.tsarsprocket.reportmid.di.qualifiers.ComputationScheduler
import com.tsarsprocket.reportmid.di.qualifiers.IoScheduler
import com.tsarsprocket.reportmid.di.qualifiers.UiScheduler
import com.tsarsprocket.reportmid.room.MainStorage
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
class ReportMidAppModule( val context: Context ) {

    @Provides
    fun context(): Context = context

    @Provides
    @IoScheduler
    fun provideIoScheduler(): Scheduler = Schedulers.io()

    @Provides
    @UiScheduler
    fun provideUiScheduler(): Scheduler = AndroidSchedulers.mainThread()

    @Provides
    @ComputationScheduler
    fun provideComputationScheduler(): Scheduler = Schedulers.computation()

    @Provides
    @Singleton
    fun provideMainStorage(): MainStorage {
        return Room.databaseBuilder(context.applicationContext, MainStorage::class.java, "database")
            .createFromAsset("database/init.db")
            .build()
    }
}