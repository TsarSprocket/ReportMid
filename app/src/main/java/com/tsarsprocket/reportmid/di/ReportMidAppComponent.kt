package com.tsarsprocket.reportmid.di

import com.tsarsprocket.reportmid.ReportMidApp
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component( modules = [
    AndroidInjectionModule::class,
    ViewModelFactoryBinder::class
] )
interface ReportMidAppComponent {

    fun inject( app: ReportMidApp )
}