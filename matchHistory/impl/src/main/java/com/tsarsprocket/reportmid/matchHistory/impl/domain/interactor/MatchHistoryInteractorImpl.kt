package com.tsarsprocket.reportmid.matchHistory.impl.domain.interactor

import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Io
import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.matchData.api.data.MatchDataRepository
import com.tsarsprocket.reportmid.matchData.api.data.model.MatchNotFoundException
import com.tsarsprocket.reportmid.matchHistory.impl.domain.model.MatchesData
import com.tsarsprocket.reportmid.utils.common.orFalse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

internal class MatchHistoryInteractorImpl @Inject constructor(
    private val matchDataRepository: MatchDataRepository,
    private val matchMapper: MatchMapper,
    @Io private val ioDispatcher: CoroutineDispatcher,
) : MatchHistoryInteractor {

    override suspend fun getMatchData(puuid: String, region: Region, startPosition: Int, count: Int): MatchesData = coroutineScope {
        val listOfMatchData = (startPosition until startPosition + count).map {
            async(ioDispatcher) {
                try {
                    matchMapper.map(matchDataRepository.getMatch(puuid, region, it), puuid)
                } catch(ex: MatchNotFoundException) {
                    null
                }
            }
        }
            .awaitAll()
            .filterNotNull()

        MatchesData(
            firstPosition = startPosition,
            matches = listOfMatchData,
            hasMoreToLoad = listOfMatchData.lastOrNull()?.isNotTheLast.orFalse(),
        )
    }
}