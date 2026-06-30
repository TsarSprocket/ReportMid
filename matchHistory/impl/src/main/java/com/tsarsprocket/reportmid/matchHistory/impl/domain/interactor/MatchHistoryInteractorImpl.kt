package com.tsarsprocket.reportmid.matchHistory.impl.domain.interactor

import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Io
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchData.api.data.MatchDataRepository
import com.tsarsprocket.reportmid.matchData.api.data.model.HasMoreHint.NO_CHANCE
import com.tsarsprocket.reportmid.matchData.api.data.model.NoMatch
import com.tsarsprocket.reportmid.matchHistory.impl.domain.model.MatchesData
import com.tsarsprocket.reportmid.matchHistory.impl.domain.model.NoMatchData
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
        val matchesWithMeta = (startPosition until startPosition + count).map {
            async(ioDispatcher) {
                matchDataRepository.getMatch(puuid, region, it)
            }
        }.awaitAll()

        val hadNoChance = matchesWithMeta.any { it.hasMoreHint == NO_CHANCE }
        val matches = matchesWithMeta
            .filter { it.match !is NoMatch }
            .map { matchMapper.map(it, puuid) }
            .let { if (hadNoChance) it + NoMatchData else it }

        MatchesData(
            firstPosition = startPosition,
            matches = matches,
            hasMoreToLoad = !hadNoChance,
        )
    }
}
