package com.tsarsprocket.reportmid.navigationMapImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchUpView.api.navigation.MatchUpViewNavigation
import com.tsarsprocket.reportmid.summonerViewApi.viewIntent.SummonerViewIntent
import com.tsarsprocket.reportmid.viewStateApi.navigation.Navigation
import com.tsarsprocket.reportmid.mainScreenApi.constants.SUMMONER_VIEW_TAG
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.getTagged
import dagger.Module
import dagger.Provides

@Module
class MatchUpViewNavigationModule {

    @Provides
    @PerApi
    @Navigation(MatchUpViewNavigation.TAG)
    fun provideMatchUpViewNavigation(): MatchUpViewNavigation = object : MatchUpViewNavigation {

        override fun ViewStateHolder.showSummonerInfo(puuid: String, region: Region) {
            val summonerViewHolder = getTagged(SUMMONER_VIEW_TAG)
                ?: throw IllegalStateException("Holder tagged '$SUMMONER_VIEW_TAG' not found")
            summonerViewHolder.postIntent(
                intent = SummonerViewIntent(puuid = puuid, region = region),
                returnIntent = summonerViewHolder.currentState.getRestoreStateIntent(),
            )
        }
    }
}
