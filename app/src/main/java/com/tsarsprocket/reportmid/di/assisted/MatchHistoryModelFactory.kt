package com.tsarsprocket.reportmid.di.assisted

import com.tsarsprocket.reportmid.model.MatchHistoryModel
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.summoner.model.SummonerModel
import dagger.assisted.AssistedFactory

@AssistedFactory
interface MatchHistoryModelFactory {
    fun create(region: Region, summoner: SummonerModel ): MatchHistoryModel
}