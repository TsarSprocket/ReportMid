package com.tsarsprocket.reportmid.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base.di.Api
import com.tsarsprocket.reportmid.base.di.AppScope
import com.tsarsprocket.reportmid.base.di.FragmentsCreator
import com.tsarsprocket.reportmid.di.qualifiers.ComputationScheduler
import com.tsarsprocket.reportmid.di.qualifiers.IoScheduler
import com.tsarsprocket.reportmid.di.qualifiers.UiScheduler
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
class ReportMidAppModule {

    @Provides
    fun provideAppContext(appApi: AppApi): Context = appApi.getAppContext()

    @Provides
    fun provideServiceFactory(lolServicesApi: LolServicesApi): ServiceFactory = lolServicesApi.getServiceFactory()

    @Provides
    fun provideFragmentCreators(
        apis: Map<Class<out Api>, @JvmSuppressWildcards Provider<Api>>,
    ): Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>> {
        return apis.entries.filter { it.key.interfaces.contains(FragmentsCreator::class.java) }
            .fold(emptyMap()) { acc, entry -> acc + (entry.value.get() as FragmentsCreator).getFragmentCreators() }
    }

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
    @AppScope
    fun provideMainStorage(context: Context): MainStorage {
        return Room.databaseBuilder(context.applicationContext, MainStorage::class.java, "database")
            .createFromAsset("database/init.db")
            .build()
    }
}