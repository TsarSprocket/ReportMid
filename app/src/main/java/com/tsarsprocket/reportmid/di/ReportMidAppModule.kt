package com.tsarsprocket.reportmid.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.room.Room
import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.appApi.di.AppContext
import com.tsarsprocket.reportmid.appApi.room.MainStorage
import com.tsarsprocket.reportmid.baseApi.di.AppScope
import com.tsarsprocket.reportmid.baseApi.di.BaseApi
import com.tsarsprocket.reportmid.baseApi.di.BindingExport
import com.tsarsprocket.reportmid.baseApi.di.FragmentsCreator
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Computation
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Io
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Ui
import com.tsarsprocket.reportmid.dataDragonApi.data.DataDragon
import com.tsarsprocket.reportmid.dataDragonApi.di.DataDragonApi
import com.tsarsprocket.reportmid.leaguePositionApi.data.LeaguePositionRepository
import com.tsarsprocket.reportmid.leaguePositionApi.di.LeaguePositionApi
import com.tsarsprocket.reportmid.lolServicesApi.di.LolServicesApi
import com.tsarsprocket.reportmid.lolServicesApi.riotapi.ServiceFactory
import com.tsarsprocket.reportmid.room.MainDatabase
import com.tsarsprocket.reportmid.stateApi.data.StateRepository
import com.tsarsprocket.reportmid.stateApi.di.StateApi
import com.tsarsprocket.reportmid.summonerApi.data.SummonerRepository
import com.tsarsprocket.reportmid.summonerApi.di.SummonerApi
import com.tsarsprocket.reportmid.viewStateApi.di.ReducerBinding
import com.tsarsprocket.reportmid.viewStateApi.reducer.ViewStateReducer
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Provider

@Module
internal class ReportMidAppModule {

    @Provides
    @AppScope
    @AppContext
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
    @AppScope
    fun provideIoScheduler(): Scheduler = Schedulers.io()

    @Provides
    @Ui
    @AppScope
    fun provideUiScheduler(): Scheduler = AndroidSchedulers.mainThread()

    @Provides
    @Computation
    @AppScope
    fun provideComputationScheduler(): Scheduler = Schedulers.computation()

    @Provides
    @Computation
    @AppScope
    fun provideComputationDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Io
    @AppScope
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Ui
    @AppScope
    fun provideUiDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @AppScope
    fun provideMainDatabase(@AppContext context: Context): MainDatabase {
        return Room.databaseBuilder(context.applicationContext, MainDatabase::class.java, "database")
            .build()
    }

    @Provides
    @AppScope
    fun provideMainStorage(db: MainDatabase): MainStorage = db

    // Temporary until migrated
    @Provides
    @AppScope
    fun provideLeaguePositionRepository(leaguePositionApi: LeaguePositionApi): LeaguePositionRepository = leaguePositionApi.getLeaguePositionRepository()

    @Provides
    @AppScope
    fun provideStateRepository(stateApi: StateApi): StateRepository = stateApi.getStateRepository()

    @Provides
    @AppScope
    fun provideSummonerRepository(summonerApi: SummonerApi): SummonerRepository = summonerApi.getSummonerRepository()

    @Provides
    @AppScope
    fun providedataDragon(dataDragonApi: DataDragonApi): DataDragon = dataDragonApi.getDataDragon()

    @Provides
    @AppScope
    fun provideFragmentFactory(baseApi: BaseApi): FragmentFactory = baseApi.getFragmentFactory()

    @Provides
    @AppScope
    fun provideViewStateReducers(@BindingExport bindingExports: @JvmSuppressWildcards Set<Any>): Map<Class<out ViewIntent>, Provider<ViewStateReducer>> {
        return bindingExports.filterIsInstance<ReducerBinding>()
            .fold(emptyMap()) { acc, entry -> acc + entry.getReducers() }
    }
}