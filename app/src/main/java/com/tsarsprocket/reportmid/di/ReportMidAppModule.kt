package com.tsarsprocket.reportmid.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base.di.AppScope
import com.tsarsprocket.reportmid.base.di.BindingExport
import com.tsarsprocket.reportmid.base.di.FragmentsCreator
import com.tsarsprocket.reportmid.base.di.qualifiers.Computation
import com.tsarsprocket.reportmid.base.di.qualifiers.Io
import com.tsarsprocket.reportmid.base.di.qualifiers.Ui
import com.tsarsprocket.reportmid.lol_services_api.di.LolServicesApi
import com.tsarsprocket.reportmid.lol_services_api.riotapi.ServiceFactory
import com.tsarsprocket.reportmid.room.MainStorage
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Provider

@Module
internal class ReportMidAppModule {

    @Provides
    @AppScope
    fun provideAppContext(appApi: AppApi): Context = appApi.getAppContext()

    @Provides
    @AppScope
    fun provideServiceFactory(lolServicesApi: LolServicesApi): ServiceFactory = lolServicesApi.getServiceFactory()

    @Provides
    @AppScope
    fun provideFragmentCreators(@BindingExport bindingExports: @JvmSuppressWildcards Set<Any>): Map<Class<out Fragment>, Provider<Fragment>> {
        return bindingExports.filterIsInstance<FragmentsCreator>()
            .fold(emptyMap()) { acc, entry -> acc + entry.getFragmentCreators() }
    }

    @Provides
    @Io
    fun provideIoScheduler(): Scheduler = Schedulers.io()

    @Provides
    @Ui
    fun provideUiScheduler(): Scheduler = AndroidSchedulers.mainThread()

    @Provides
    @Computation
    fun provideComputationScheduler(): Scheduler = Schedulers.computation()

    @Provides
    @AppScope
    fun provideMainStorage(context: Context): MainStorage {
        return Room.databaseBuilder(context.applicationContext, MainStorage::class.java, "database")
            .createFromAsset("database/init.db")
            .build()
    }
}