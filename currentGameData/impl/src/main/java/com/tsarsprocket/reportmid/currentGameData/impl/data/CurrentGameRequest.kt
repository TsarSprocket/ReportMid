package com.tsarsprocket.reportmid.currentGameData.impl.data

import com.tsarsprocket.reportmid.currentGameData.impl.retrofit.SpectatorV5
import com.tsarsprocket.reportmid.currentGameData.impl.retrofit.dto.NoMatchFound
import com.tsarsprocket.reportmid.currentGameData.impl.retrofit.dto.SpectatorResponse
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.lolServicesApi.riotapi.ServiceFactory
import com.tsarsprocket.reportmid.lolServicesApi.riotapi.getService
import com.tsarsprocket.reportmid.requestManagerApi.data.Request
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException

internal class CurrentGameRequest @AssistedInject constructor(
    @Assisted puuid: String,
    @Assisted private val region: Region,
    private val serviceFactory: ServiceFactory,
) : Request<CurrentGameInfoKey, SpectatorResponse>(CurrentGameInfoKey(puuid)) {

    override suspend fun invoke(): SpectatorResponse {
        val response = serviceFactory.getService<SpectatorV5>(region).getByPuuid(key.puuid)
        return when {
            response.isSuccessful -> response.body()!!
            response.code() == NOT_FOUND -> NoMatchFound
            else -> throw HttpException(response)
        }
    }

    @AssistedFactory
    interface Factory {
        fun createRequest(puuid: String, region: Region): CurrentGameRequest
    }

    companion object {
        const val NOT_FOUND = 404
    }
}