package com.tsarsprocket.reportmid.matchDetails.impl.domain.interactor

import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.matchDetails.impl.domain.model.MatchDetailsData

internal interface MatchDetailsInteractor {
    suspend fun getMatchDetails(matchId: String, region: Region): MatchDetailsData
}