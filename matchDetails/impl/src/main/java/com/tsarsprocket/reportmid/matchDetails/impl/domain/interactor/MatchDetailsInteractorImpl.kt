package com.tsarsprocket.reportmid.matchDetails.impl.domain.interactor

import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Computation
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchData.api.data.MatchDataRepository
import com.tsarsprocket.reportmid.matchData.api.data.model.ActualMatch
import com.tsarsprocket.reportmid.matchDetails.impl.domain.model.MatchDetailsData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class MatchDetailsInteractorImpl @Inject constructor(
    private val repository: MatchDataRepository,
    private val mapper: MatchToMatchDetailsDataMapper,
    @param:Computation
    private val dispatcher: CoroutineDispatcher,
) : MatchDetailsInteractor {

    override suspend fun getMatchDetails(matchId: String, region: Region): MatchDetailsData = withContext(dispatcher) {
        val match = repository.getMatch(matchId, region) as? ActualMatch ?: error("Match not found: $matchId")
        mapper.map(match, region)
    }
}