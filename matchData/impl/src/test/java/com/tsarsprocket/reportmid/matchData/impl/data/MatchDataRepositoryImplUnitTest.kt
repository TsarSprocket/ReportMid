package com.tsarsprocket.reportmid.matchData.impl.data

import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.matchData.api.data.model.Match
import com.tsarsprocket.reportmid.matchData.impl.data.model.MatchIdPage
import com.tsarsprocket.reportmid.matchData.impl.data.model.MatchIdPageKey
import com.tsarsprocket.reportmid.matchData.impl.retrofit.MatchDto
import com.tsarsprocket.reportmid.requestManagerApi.data.RequestManager
import com.tsarsprocket.reportmid.utils.common.EMPTY_STRING
import com.tsarsprocket.reportmid.utilsTest.MainTestDispatcherExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.RegisterExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.same
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.kotlin.wheneverBlocking

@Suppress("MemberVisibilityCanBePrivate")
internal class MatchDataRepositoryImplUnitTest {

    val listOfMatchIds = List(PAGE_SIZE) { if(it == POSITION % PAGE_SIZE) MATCH_ID else EMPTY_STRING }
    val match: Match = mock()
    val matchDto: MatchDto = mock()
    val matchIdPage: MatchIdPage = mock()
    val matchIdPageRequest: MatchIdPageRequest = mock()
    val matchIdPageRequestFactory: MatchIdPageRequest.Factory = mock()
    val matchRequest: MatchRequest = mock()
    val matchRequestFactory: MatchRequest.Factory = mock()
    val matchModelMapper: MatchModelMapper = mock()
    val requestManager: RequestManager = mock()

    lateinit var matchDataRepository: MatchDataRepositoryImpl

    @BeforeEach
    fun setup() {
        whenever(matchIdPageRequestFactory.createRequest(any())).thenReturn(matchIdPageRequest)
        whenever(matchRequestFactory.createRequest(any(), any())).thenReturn(matchRequest)

        matchDataRepository = MatchDataRepositoryImpl(requestManager, matchIdPageRequestFactory, matchRequestFactory, matchModelMapper, mainTestDispatcherExtension.dispatcher)
    }

    @Test
    fun `get match data successfully having empty cache`() = runTest {
        whenever(matchModelMapper.map(any())).thenReturn(match)
        whenever(matchIdPage.matchIds).thenReturn(listOfMatchIds)
        wheneverBlocking { requestManager.request(any(), eq(MatchIdPage::class)) }.thenReturn(matchIdPage)
        wheneverBlocking { requestManager.request(any(), eq(MatchDto::class)) }.thenReturn(matchDto)

        val receivedMatch = matchDataRepository.getMatch(PUUID, testRegion, POSITION)

        verify(matchIdPageRequestFactory).createRequest(argThat { puuid == PUUID && this.region == region && pageNo == POSITION / PAGE_SIZE })
        verify(matchRequestFactory).createRequest(eq(MATCH_ID), same(testRegion))
        verify(matchModelMapper).map(same(matchDto))
        assert(receivedMatch === match)
    }

    @Test
    fun `get match data from cache`() = runTest {
        matchDataRepository.matchIdPageCache.put(MatchIdPageKey(PUUID, testRegion, POSITION / PAGE_SIZE), listOfMatchIds)
        matchDataRepository.matchModelCache.put(MATCH_ID, match)

        val receivedMatch = matchDataRepository.getMatch(PUUID, testRegion, POSITION)

        verify(matchIdPageRequestFactory, never()).createRequest(any())
        verify(matchRequestFactory, never()).createRequest(any(), any())
        verify(matchModelMapper, never()).map(any())
        assert(receivedMatch == match)
    }

    @Test
    fun `get match data with error having obtaining match ids unsuccessful`() = runTest {
        wheneverBlocking { requestManager.request(any(), eq(MatchIdPage::class)) }.thenThrow(RuntimeException())

        assertThrows<RuntimeException> {
            matchDataRepository.getMatch(PUUID, testRegion, POSITION)
        }

        verify(matchIdPageRequestFactory).createRequest(argThat { puuid == PUUID && this.region == region && pageNo == POSITION / PAGE_SIZE })
        verify(matchRequestFactory, never()).createRequest(any(), any())
        verify(matchModelMapper, never()).map(any())
    }

    @Test
    fun `get match data with error having obtaining match data unsuccessful`() = runTest {
        whenever(matchIdPage.matchIds).thenReturn(listOfMatchIds)
        wheneverBlocking { requestManager.request(any(), eq(MatchIdPage::class)) }.thenReturn(matchIdPage)
        wheneverBlocking { requestManager.request(any(), eq(MatchDto::class)) }.thenThrow(RuntimeException())

        assertThrows<RuntimeException> {
            matchDataRepository.getMatch(PUUID, testRegion, POSITION)
        }

        verify(matchIdPageRequestFactory).createRequest(argThat { puuid == PUUID && this.region == region && pageNo == POSITION / PAGE_SIZE })
        verify(matchRequestFactory).createRequest(eq(MATCH_ID), same(testRegion))
        verify(matchModelMapper, never()).map(any())
    }

    companion object {
        const val MATCH_ID = "match_ID"
        const val POSITION = PAGE_SIZE * 3 + PAGE_SIZE / 2
        const val PUUID = "puuid"

        val testRegion = Region.EUROPE_WEST

        @RegisterExtension
        val mainTestDispatcherExtension = MainTestDispatcherExtension()
    }
}