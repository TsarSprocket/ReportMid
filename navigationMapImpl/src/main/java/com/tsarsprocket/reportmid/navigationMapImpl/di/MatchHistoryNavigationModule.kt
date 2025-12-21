package com.tsarsprocket.reportmid.navigationMapImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchDetails.api.viewIntent.MatchDetailsIntent
import com.tsarsprocket.reportmid.matchHistory.api.navigation.MatchHistoryNavigation
import com.tsarsprocket.reportmid.summonerViewApi.viewState.SummonerViewStateReturnPoint
import com.tsarsprocket.reportmid.viewStateApi.navigation.Navigation
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import dagger.Module
import dagger.Provides

@Module
class MatchHistoryNavigationModule {

    @Provides
    @PerApi
    @Navigation(MatchHistoryNavigation.TAG)
    fun provideMatchHistoryNavigation(): MatchHistoryNavigation = object : MatchHistoryNavigation {

        override fun ViewStateHolder.showMatchDetails(region: Region, matchId: String) {
            val targetHolder = generateSequence(seed = this) { it.parentHolder }.find { it.viewStates.value is SummonerViewStateReturnPoint }
                ?: throw IllegalStateException("Holder of ${SummonerViewStateReturnPoint::class.simpleName} not found starting $this")

            targetHolder.postIntent(
                intent = MatchDetailsIntent(matchId = matchId, region = region),
                returnIntent = targetHolder.viewStates.value.getRestoreStateIntent(),
            )
        }
    }
}