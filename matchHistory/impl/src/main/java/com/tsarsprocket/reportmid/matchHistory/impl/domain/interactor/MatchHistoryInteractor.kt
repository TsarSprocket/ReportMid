package com.tsarsprocket.reportmid.matchHistory.impl.domain.interactor

import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.matchHistory.impl.domain.model.MatchesData

internal interface MatchHistoryInteractor {
    suspend fun getMatchData(puuid: String, region: Region, startPosition: Int, count: Int): MatchesData
}