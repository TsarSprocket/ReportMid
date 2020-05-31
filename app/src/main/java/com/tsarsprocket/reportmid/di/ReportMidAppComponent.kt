package com.tsarsprocket.reportmid.di

import android.content.Context
import com.tsarsprocket.reportmid.InitialEnterFragment
import com.tsarsprocket.reportmid.LandingFragment
import com.tsarsprocket.reportmid.ReportMidApp
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component( modules = [
    AndroidInjectionModule::class,
    ReportMidAppModule::class,
    MainActivityModule::class,
    ViewModelFactoryBinder::class,
    InitialEnterFragmentModule::class,
    LandingViewModelModule::class
] )
interface ReportMidAppComponent {

    fun context(): Context

    // Application
    fun inject( app: ReportMidApp )

    // Viewmodel
    fun landingViewModelComponentFactory(): LandingViewModelComponent.Factory

    // Fragments
    fun inject( landingFragment: LandingFragment )
    fun inject( initialEnterFragment: InitialEnterFragment )
}