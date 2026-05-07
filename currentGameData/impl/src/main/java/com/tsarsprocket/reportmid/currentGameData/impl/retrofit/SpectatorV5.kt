package com.tsarsprocket.reportmid.currentGameData.impl.retrofit

import com.tsarsprocket.reportmid.currentGameData.impl.retrofit.dto.CurrentGameInfoDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

internal interface SpectatorV5 {

    @GET("/lol/spectator/v5/active-games/by-summoner/{$PUUID}")
    suspend fun getByPuuid(@Path(PUUID) puuid: String): Response<CurrentGameInfoDto>

    private companion object {
        const val PUUID = "puuid"
    }
}