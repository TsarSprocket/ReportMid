package com.tsarsprocket.reportmid.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ReportMidAppModule( val context: Context ) {

    @Provides
    fun context(): Context = context
}