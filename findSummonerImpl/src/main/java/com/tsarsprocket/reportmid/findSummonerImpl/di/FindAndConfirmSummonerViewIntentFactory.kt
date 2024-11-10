package com.tsarsprocket.reportmid.findSummonerImpl.di

import com.tsarsprocket.reportmid.findSummonerImpl.viewIntent.FindAndConfirmSummonerViewIntent
import com.tsarsprocket.reportmid.lol.model.Region
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
internal interface FindAndConfirmSummonerViewIntentFactory {
    fun create(@Assisted("gameName") gameName: String, @Assisted("tagline") tagline: String, region: Region): FindAndConfirmSummonerViewIntent
}