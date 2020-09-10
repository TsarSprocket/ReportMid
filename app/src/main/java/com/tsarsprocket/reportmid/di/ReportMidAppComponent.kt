package com.tsarsprocket.reportmid.di

import android.content.Context
import com.tsarsprocket.reportmid.*
import com.tsarsprocket.reportmid.controller.*
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component( modules = [
    AndroidInjectionModule::class,
    ReportMidAppModule::class,
    LandingActivityModule::class,
    MainActivityModule::class,
    ViewModelFactoryBinder::class,
    DrawerViewModelModule::class,
    LandingViewModelModule::class,
    AddSummonerViewModelModule::class,
    ConfirmSummonerViewModelModule::class,
    ProfileOverviewViewModelModule::class,
    MatchupViewModelModule::class,
    MatchHistoryViewModelModule::class
] )
interface ReportMidAppComponent {

    fun context(): Context

    // Application
    fun inject( app: ReportMidApp )

    // Viewmodel
    fun drawerViewModelComponent(): DrawerViewModelComponent.Factory
    fun landingViewModelComponent(): LandingViewModelComponent.Factory
    fun addSummonerViewModelComponentFactory(): AddSummonerViewModelComponent.Factory
    fun confirmSummonerViewModelComponentFactory(): ConfirmSummonerViewModelComponent.Factory
    fun profileOverviewViewModelComponentFactory(): ProfileOverviewViewModelComponent.Factory
    fun matchupViewModelComponentFactory(): MatchupViewModelComponent.Factory
    fun matchHistoryViewModelComponentFactory(): MatchHistoryViewModelComponent.Factory

    // Fragments
    fun inject( drawerFragment: DrawerFragment)
    fun inject( landingFragment: LandingFragment)
    fun inject( addSummonerFragment: AddSummonerFragment )
    fun inject( confirmSummonerFragment: ConfirmSummonerFragment )
    fun inject( profileOverviewFragment: ProfileOverviewFragment)
    fun inject( matchHistoryFragment: MatchHistoryFragment)
    fun inject( matchupFragment: MatchupFragment)
}