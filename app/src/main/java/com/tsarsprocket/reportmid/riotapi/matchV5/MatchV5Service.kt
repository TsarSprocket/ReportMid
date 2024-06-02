package com.tsarsprocket.reportmid.riotapi.matchV5

import com.tsarsprocket.reportmid.lolServicesApi.riotapi.CallByType
import com.tsarsprocket.reportmid.lolServicesApi.riotapi.ServerInfo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@ServerInfo( callBy = CallByType.SUPER_REGION )
interface MatchV5Service {
    @GET("lol/match/v5/matches/by-puuid/{puuid}/ids")
    fun matches(
        @Path("puuid") puuid: String,
        @Query("startTime") startTime: Long? = null,
        @Query("endTime") endTime: Long? = null,
        @Query("queue") queue: Int? = null,
        @Query("type") type: String? = null,
        @Query("start") start: Int? = null,
        @Query("count") count: Int? = null,
    ): Observable<List<String>>

    @GET("lol/match/v5/matches/{matchId}")
    fun match(@Path("matchId") matchId: String): Observable<MatchDto>
}
