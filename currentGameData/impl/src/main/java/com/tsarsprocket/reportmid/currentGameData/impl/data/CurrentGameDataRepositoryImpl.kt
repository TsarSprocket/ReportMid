package com.tsarsprocket.reportmid.currentGameData.impl.data

import com.tsarsprocket.reportmid.currentGameData.api.data.CurrentGameDataRepository
import com.tsarsprocket.reportmid.currentGameData.api.data.model.CurrentGame
import com.tsarsprocket.reportmid.currentGameData.impl.retrofit.dto.SpectatorResponse
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.requestManagerApi.data.RequestManager
import com.tsarsprocket.reportmid.requestManagerApi.data.request
import javax.inject.Inject

internal class CurrentGameDataRepositoryImpl @Inject constructor(
    private val currentGameRequestFactory: CurrentGameRequest.Factory,
    private val requestManager: RequestManager,
    private val mapper: CurrentGameMapper,
) : CurrentGameDataRepository {

    override suspend fun getCurrentGameData(
        puuid: String,
        region: Region
    ): CurrentGame {
        val dto = requestManager.request<SpectatorResponse>(currentGameRequestFactory.createRequest(puuid, region))
        return mapper.map(dto)
    }
}