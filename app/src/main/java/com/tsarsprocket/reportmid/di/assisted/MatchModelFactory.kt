package com.tsarsprocket.reportmid.di.assisted

import com.tsarsprocket.reportmid.model.MatchModel
import com.tsarsprocket.reportmid.model.RegionModel
import com.tsarsprocket.reportmid.riotapi.matchV4.MatchDto
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface MatchModelFactory {
    fun create( matchDto: MatchDto, region: RegionModel ): MatchModel
}