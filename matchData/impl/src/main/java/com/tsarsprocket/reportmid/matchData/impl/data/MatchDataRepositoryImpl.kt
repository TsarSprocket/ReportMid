package com.tsarsprocket.reportmid.matchData.impl.data

import com.mayakapps.kache.InMemoryKache
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Computation
import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.matchData.api.data.MatchDataRepository
import com.tsarsprocket.reportmid.matchData.api.data.model.Match
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

    override suspend fun getMatch(puuid: String, region: Region, position: Int): Match = withContext(dispatcher) {
        val matchIdPageKey = MatchIdPageKey(puuid, region, position / PAGE_SIZE)
        val matchId = matchIdPageCache.getOrPut(matchIdPageKey) {
            requestManager.request<MatchIdPage>(matchIdPageRequestFactory.createRequest(matchIdPageKey)).matchIds
        }!![position % PAGE_SIZE]
        matchModelCache.getOrPut(matchId) {
            matchModelMapper.map(requestManager.request<MatchDto>(matchRequestFactory.createRequest(matchId, region)))
        }!!
    }

    companion object {
        const val MAX_PAGE_CACHE_SIZE = 100L
        const val MATCH_EXPIRATION_MINUTES = 5
        const val PAGE_EXPIRATION_MINUTES = 5
    }
}