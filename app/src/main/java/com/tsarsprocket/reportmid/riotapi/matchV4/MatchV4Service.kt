package com.tsarsprocket.reportmid.riotapi.matchV4

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MatchV4Service {
    @GET("lol/match/v4/matchlists/by-account/{accountId}")
    fun matchList(@Path("accountId") accountId: String,
                  @Query("champion") champion: Set<Int>? = null,
                  @Query("queue") queue: Set<Int>? = null,
                  @Query("season") season: Set<Int>? = null,
                  @Query("beginTime") beginTime: Long? = null,
                  @Query("endTime") endTime: Long? = null,
                  @Query("beginIndex") beginIndex: Int? = null,
                  @Query("endIndex") endIndex: Int? = null,
    ): Observable<MatchListDto>

    @GET("lol/match/v4/matches/{matchId}")
    fun match(@Path("matchId") matchId: Long): Observable<MatchDto>
}
