package com.tsarsprocket.reportmid.matchData.impl.data

import com.mayakapps.kache.InMemoryKache
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Computation
import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.matchData.api.data.MatchDataRepository
import com.tsarsprocket.reportmid.matchData.api.data.model.HasMoreHint
import com.tsarsprocket.reportmid.matchData.api.data.model.HasMoreHint.NO_CHANCE
import com.tsarsprocket.reportmid.matchData.api.data.model.Match
import com.tsarsprocket.reportmid.matchData.api.data.model.MatchNotFoundException
import com.tsarsprocket.reportmid.matchData.impl.data.model.MatchIdPage
import com.tsarsprocket.reportmid.matchData.impl.data.model.MatchIdPageKey
import com.tsarsprocket.reportmid.matchData.impl.retrofit.MatchDto
import com.tsarsprocket.reportmid.requestManagerApi.data.RequestManager
import com.tsarsprocket.reportmid.requestManagerApi.data.request
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.VisibleForTesting
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

internal class MatchDataRepositoryImpl @Inject constructor(
    private val requestManager: RequestManager,
    private val matchIdPageRequestFactory: MatchIdPageRequest.Factory,
    private val matchRequestFactory: MatchRequest.Factory,
    private val matchModelMapper: MatchModelMapper,
    @Computation private val dispatcher: CoroutineDispatcher,
) : MatchDataRepository {

    @VisibleForTesting
    internal val matchIdPageCache = InMemoryKache<MatchIdPageKey, List<String>>(MAX_PAGE_CACHE_SIZE) {
        expireAfterWriteDuration = PAGE_EXPIRATION_MINUTES.minutes
    }

    @VisibleForTesting
    internal val matchModelCache = InMemoryKache<String, Match>(MAX_PAGE_CACHE_SIZE) {
        expireAfterWriteDuration = MATCH_EXPIRATION_MINUTES.minutes
    }

    /**
     * Return the match with the given id or throw a [MatchNotFoundException] if it is not found
     * @param puuid the puuid of the player
     * @param region the region of the player
     * @param position the position of the match in the list of matches
     * @throws MatchNotFoundException if the match is not found
     */
    override suspend fun getMatch(puuid: String, region: Region, position: Int): Match = withContext(dispatcher) {
        val matchIdPageKey = MatchIdPageKey(puuid, region, position / PAGE_SIZE)
        val matchIdsPage = matchIdPageCache.getOrPut(matchIdPageKey) {
            requestManager.request<MatchIdPage>(matchIdPageRequestFactory.createRequest(matchIdPageKey)).matchIds
        }!!
        val positionOnPage = position % PAGE_SIZE

        if(positionOnPage >= matchIdsPage.size) throw MatchNotFoundException()

        val matchId = matchIdsPage[positionOnPage]
        val hasMoreHint = when {
            matchIdsPage.size < PAGE_SIZE && positionOnPage == matchIdsPage.size.dec() -> NO_CHANCE // The page is shorter and we are at the end of it
            positionOnPage < matchIdsPage.size - 1 -> HasMoreHint.DEFINITELY // We are not at the end of the page yet
            else -> HasMoreHint.LIKELY // The page is full but we are at the end of it - who knows if there are more
        }
        matchModelCache.getOrPut(matchId) {
            matchModelMapper.map(requestManager.request<MatchDto>(matchRequestFactory.createRequest(matchId, region)), hasMoreHint)
        }!!
    }

    companion object {
        const val MAX_PAGE_CACHE_SIZE = 100L
        const val MATCH_EXPIRATION_MINUTES = 5
        const val PAGE_EXPIRATION_MINUTES = 5
    }
}