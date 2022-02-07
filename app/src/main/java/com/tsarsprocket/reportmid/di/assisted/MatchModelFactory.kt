package com.tsarsprocket.reportmid.di.assisted

import com.tsarsprocket.reportmid.model.MatchModel
import com.tsarsprocket.reportmid.model.RegionModel
import com.tsarsprocket.reportmid.riotapi.matchV5.MatchDto
import dagger.assisted.AssistedFactory

@AssistedFactory
interface MatchModelFactory {
    fun create( matchDto: MatchDto, region: RegionModel ): MatchModel
}