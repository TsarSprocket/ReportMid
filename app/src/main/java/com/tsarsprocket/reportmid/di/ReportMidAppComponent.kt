package com.tsarsprocket.reportmid.di

import android.content.Context
import com.tsarsprocket.reportmid.*
import com.tsarsprocket.reportmid.controller.*
import com.tsarsprocket.reportmid.viewmodel.ManageFriendsViewModel
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ReportMidAppModule::class,
        LandingActivityModule::class,
        MainActivityModule::class,
        MainActivityViewModelModule::class,
        ViewModelFactoryBinder::class,
        DrawerViewModelModule::class,
        LandingViewModelModule::class,
        AddSummonerViewModelModule::class,
        ConfirmSummonerViewModelModule::class,
        ProfileOverviewViewModelModule::class,
        MatchupViewModelModule::class,
        MatchHistoryViewModelModule::class,
        ManageSummonersViewModelModule::class,
        ManageFriendsViewModelModule::class,
    ]
)
interface ReportMidAppComponent {

    fun context(): Context

    // Application
    fun inject(app: ReportMidApp)

    // Viewmodel
    fun mainActivityViewModelComponent(): MainActivityViewModelComponent.Factory
    fun drawerViewModelComponent(): DrawerViewModelComponent.Factory
    fun landingViewModelComponent(): LandingViewModelComponent.Factory
    fun addSummonerViewModelComponentFactory(): AddSummonerViewModelComponent.Factory
    fun confirmSummonerViewModelComponentFactory(): ConfirmSummonerViewModelComponent.Factory
    fun profileOverviewViewModelComponentFactory(): ProfileOverviewViewModelComponent.Factory
    fun matchupViewModelComponentFactory(): MatchupViewModelComponent.Factory
    fun matchHistoryViewModelComponentFactory(): MatchHistoryViewModelComponent.Factory
    fun manageMySummonersViewModelComponentFactory(): ManageSummonersViewModelComponent.Factory
    fun manageFriendsViewModelComponentFactory(): ManageFriendsViewModelComponent.Factory

    // Fragments
    fun inject(drawerFragment: DrawerFragment)
    fun inject(landingFragment: LandingFragment)
    fun inject(addSummonerFragment: AddSummonerFragment)
    fun inject(confirmSummonerFragment: ConfirmSummonerFragment)
    fun inject(profileOverviewFragment: ProfileOverviewFragment)
    fun inject(matchHistoryFragment: MatchHistoryFragment)
    fun inject(matchupFragment: MatchupFragment)
    fun inject(manageSummonersFragment: ManageSummonersFragment)
    fun inject(manageFriendsFragment: ManageFriendsFragment)
}