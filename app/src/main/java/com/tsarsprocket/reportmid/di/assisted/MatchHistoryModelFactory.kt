package com.tsarsprocket.reportmid.di.assisted

import com.tsarsprocket.reportmid.model.MatchHistoryModel
import com.tsarsprocket.reportmid.model.RegionModel
import com.tsarsprocket.reportmid.summoner.model.SummonerModel
import dagger.assisted.AssistedFactory

@AssistedFactory
interface MatchHistoryModelFactory {
    fun create( region: RegionModel, summoner: SummonerModel ): MatchHistoryModel
}