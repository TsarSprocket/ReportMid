package com.tsarsprocket.reportmid.matchData.impl.retrofit

import com.tsarsprocket.reportmid.lolServicesApi.riotapi.CallByType
import com.tsarsprocket.reportmid.lolServicesApi.riotapi.ServerInfo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@ServerInfo(callBy = CallByType.SUPER_REGION)
internal interface MatchV5 {

    @GET("/lol/match/v5/matches/by-puuid/{$PUUID}/ids")
    suspend fun getByPuuid(@Path(PUUID) puuid: String, @Query(START) start: Int = 0, @Query(COUNT) count: Int = 20): List<String>

    @GET("/lol/match/v5/matches/{$MATCH_ID}")
    suspend fun getByMatchId(@Path(MATCH_ID) matchId: String): MatchDto

    private companion object {
        const val COUNT = "count"
        const val MATCH_ID = "matchId"
        const val PUUID = "puuid"
        const val START = "start"
    }
}