package com.tsarsprocket.reportmid.summonerImpl.retrofit.summoner

import retrofit2.http.GET
import retrofit2.http.Path

interface SummonerV4Service {

    @GET("lol/summoner/v4/summoners/by-account/{accountId}")
    suspend fun getByAccountId(@Path("accountId") accountId: String): SummonerDto

    @GET("lol/summoner/v4/summoners/by-puuid/{puuid}")
    suspend fun getByPuuid(@Path("puuid") puuid: String): SummonerDto

    @GET("lol/summoner/v4/summoners/{summonerId}")
    suspend fun getBySummonerId(@Path("summonerId") summonerId: String): SummonerDto

    @GET("lol/summoner/v4/summoners/by-name/{summonerName}")
    suspend fun getBySummonerName(@Path("summonerName") summonerName: String): SummonerDto
}