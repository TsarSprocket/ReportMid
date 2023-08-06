package com.tsarsprocket.reportmid.app.di

import android.content.Context
import com.tsarsprocket.reportmid.ReportMidApp
import dagger.Module
import dagger.Provides

@Module
internal class AppModule {

    @Provides
    fun provideAppContext(): Context = ReportMidApp.instance
}
