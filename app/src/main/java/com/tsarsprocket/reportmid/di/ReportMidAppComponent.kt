package com.tsarsprocket.reportmid.di

import android.content.Context
import com.tsarsprocket.reportmid.ReportMidApp
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component( modules = [
    ReportMidAppModule::class,
    AndroidInjectionModule::class,
    ViewModelFactoryBinder::class
] )
interface ReportMidAppComponent {

    fun context(): Context

    fun inject( app: ReportMidApp )
}