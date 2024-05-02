package com.tsarsprocket.reportmid.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.app_api.di.AppContext
import com.tsarsprocket.reportmid.app_api.room.MainStorage
import com.tsarsprocket.reportmid.base.di.AppScope
import com.tsarsprocket.reportmid.base.di.BindingExport
import com.tsarsprocket.reportmid.base.di.FragmentsCreator
import com.tsarsprocket.reportmid.base.di.qualifiers.Computation
import com.tsarsprocket.reportmid.base.di.qualifiers.Io
import com.tsarsprocket.reportmid.base.di.qualifiers.Ui
import com.tsarsprocket.reportmid.data_dragon_api.data.DataDragon
import com.tsarsprocket.reportmid.data_dragon_api.di.DataDragonApi
import com.tsarsprocket.reportmid.league_position_api.data.LeaguePositionRepository
import com.tsarsprocket.reportmid.league_position_api.di.LeaguePositionApi
import com.tsarsprocket.reportmid.lol_services_api.di.LolServicesApi
import com.tsarsprocket.reportmid.lol_services_api.riotapi.ServiceFactory
import com.tsarsprocket.reportmid.room.MainDatabase
import com.tsarsprocket.reportmid.state_api.data.StateRepository
import com.tsarsprocket.reportmid.state_api.di.StateApi
import com.tsarsprocket.reportmid.summoner_api.data.SummonerRepository
import com.tsarsprocket.reportmid.summoner_api.di.SummonerApi
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
    fun provide(dataDragonApi: DataDragonApi): DataDragon = dataDragonApi.getDataDragon()
}