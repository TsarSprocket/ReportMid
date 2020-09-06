package com.tsarsprocket.reportmid.di

import android.content.Context
import com.tsarsprocket.reportmid.*
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component( modules = [
    AndroidInjectionModule::class,
    ReportMidAppModule::class,
    MainActivityModule::class,
    ViewModelFactoryBinder::class,
    LandingViewModelModule::class,
    InitialEntryViewModelModule::class,
    ProfileOverviewViewModelModule::class,
    MatchupViewModelModule::class,
    MatchHistoryViewModelModule::class
] )
interface ReportMidAppComponent {

    fun context(): Context

    // Application
    fun inject( app: ReportMidApp )

    // Viewmodel
    fun landingViewModelComponent(): LandingViewModelComponent.Factory
    fun initialEntryViewModelComponentFactory(): InitialEntryViewModelComponent.Factory
    fun profileOverviewViewModelComponentFactory(): ProfileOverviewViewModelComponent.Factory
    fun matchupViewModelComponentFactory(): MatchupViewModelComponent.Factory
    fun matchHistoryViewModelComponentFactory(): MatchHistoryViewModelComponent.Factory

    // Fragments
    fun inject( landingFragment: LandingFragment )
    fun inject( initialEnterFragment: InitialEnterFragment )
    fun inject( profileOverviewFragment: ProfileOverviewFragment )
    fun inject( matchHistoryFragment: MatchHistoryFragment )
    fun inject( matchupFragment: MatchupFragment )
}