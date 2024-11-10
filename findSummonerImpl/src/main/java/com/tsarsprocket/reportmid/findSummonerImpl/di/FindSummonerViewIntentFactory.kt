package com.tsarsprocket.reportmid.findSummonerImpl.di

import com.tsarsprocket.reportmid.findSummonerApi.viewIntent.FindSummonerResult
import com.tsarsprocket.reportmid.findSummonerApi.viewIntent.FindSummonerViewIntent
import com.tsarsprocket.reportmid.findSummonerImpl.viewIntent.FindSummonerViewIntentImpl
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ReturnIntentFactory
import dagger.assisted.AssistedFactory

@AssistedFactory
internal interface FindSummonerViewIntentFactory : FindSummonerViewIntent.Factory {
    override fun create(returnIntentFactory: ReturnIntentFactory<FindSummonerResult>, removeRecentState: Boolean): FindSummonerViewIntentImpl
}
