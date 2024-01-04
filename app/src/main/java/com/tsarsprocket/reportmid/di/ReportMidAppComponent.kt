package com.tsarsprocket.reportmid.di

import com.tsarsprocket.reportmid.ReportMidApp
import com.tsarsprocket.reportmid.app.di.AppProvisionModule
import com.tsarsprocket.reportmid.app_api.room.MainStorage
import com.tsarsprocket.reportmid.base.di.AppScope
import com.tsarsprocket.reportmid.controller.AddSummonerFragment
import com.tsarsprocket.reportmid.controller.ConfirmSummonerFragment
import com.tsarsprocket.reportmid.controller.DrawerFragment
import com.tsarsprocket.reportmid.controller.LandingFragment
import com.tsarsprocket.reportmid.controller.ManageFriendsFragment
import com.tsarsprocket.reportmid.controller.ManageSummonersFragment
import com.tsarsprocket.reportmid.controller.MatchHistoryFragment
import com.tsarsprocket.reportmid.controller.MatchupFragment
import com.tsarsprocket.reportmid.data_dragon_impl.di.DataDragonProvisioningModule
import com.tsarsprocket.reportmid.landing_impl.di.LandingProvisionModule
import com.tsarsprocket.reportmid.league_position_impl.di.LeaguePositionProvisionModule
import com.tsarsprocket.reportmid.lol_services_impl.di.LolServicesProvisionModule
import com.tsarsprocket.reportmid.overview.controller.ProfileOverviewFragment
import com.tsarsprocket.reportmid.request_manager.di.RequestManagerModule
import com.tsarsprocket.reportmid.room.MainDatabase
import com.tsarsprocket.reportmid.summoner.di.SummonerModule
import com.tsarsprocket.reportmid.view_state_impl.di.ViewStateProvisionModule
import dagger.Component
import dagger.android.AndroidInjectionModule

// TODO: After completing migrating to the capabilities, this component will only be used for the root graph creation
@AppScope
@Component(
    modules = [
        ViewModelFactoryModule::class,
        AndroidInjectionModule::class,
        ReportMidAppModule::class,
        LandingActivityModule::class,
        MainActivityModule::class,
        MainActivityViewModelModule::class,
        DrawerViewModelModule::class,
        LandingViewModelModule::class,
        AddSummonerViewModelModule::class,
        ConfirmSummonerViewModelModule::class,
        ProfileOverviewViewModelModule::class,
        MatchupViewModelModule::class,
        MatchHistoryViewModelModule::class,
        ManageSummonersViewModelModule::class,
        ManageFriendsViewModelModule::class,
        SummonerModule::class,
        RequestManagerModule::class,
        // Api provision modules
        AppProvisionModule::class,
        DataDragonProvisioningModule::class,
        LandingProvisionModule::class,
        LeaguePositionProvisionModule::class,
        LolServicesProvisionModule::class,
        ViewStateProvisionModule::class,
    ]
)
interface ReportMidAppComponent {

    // Application
    fun inject(app: ReportMidApp)

    fun getMainDatabase(): MainDatabase // TODO: Remove after the migration of DB to individual modules is complete
    fun getMainStorage(): MainStorage

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