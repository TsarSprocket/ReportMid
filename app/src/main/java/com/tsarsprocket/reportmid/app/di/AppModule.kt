package com.tsarsprocket.reportmid.app.di

import android.content.Context
import androidx.fragment.app.Fragment
import com.tsarsprocket.reportmid.ReportMidApp
import com.tsarsprocket.reportmid.appApi.di.AppContext
import com.tsarsprocket.reportmid.appApi.room.MainStorage
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Aggregated
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Computation
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Io
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Ui
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Provider

@Module
internal class AppModule {

    @Provides
    @PerApi
    @AppContext
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
    @Aggregated
    fun provideFragmentCreators(): Map<Class<out Fragment>, Provider<Fragment>> = ReportMidApp.instance.comp.getFragmentCreators()
}
