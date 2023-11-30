package com.tsarsprocket.reportmid.league_position_impl.retrofit

import retrofit2.http.GET
import retrofit2.http.Path

interface LeagueV4Service {

    @GET("lol/league/v4/entries/by-summoner/{encryptedSummonerId}")
    suspend fun getEntriesBySummonerId(@Path("encryptedSummonerId") summonerId: String): List<LeaguePositionDto>?
}