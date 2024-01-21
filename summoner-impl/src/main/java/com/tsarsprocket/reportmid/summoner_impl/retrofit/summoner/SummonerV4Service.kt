package com.tsarsprocket.reportmid.summoner_impl.retrofit.summoner

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface SummonerV4Service {

    @GET("lol/summoner/v4/summoners/by-account/{accountId}")
    fun getByAccountId(@Path("accountId" ) accountId: String): Single<SummonerDto>

    @GET("lol/summoner/v4/summoners/by-puuid/{puuid}")
    fun getByPuuid(@Path("puuid") puuid: String ): Single<SummonerDto>

    @GET("lol/summoner/v4/summoners/{summonerId}")
    fun getBySummonerId( @Path("summonerId") summonerId: String ): Single<SummonerDto>

    @GET("lol/summoner/v4/summoners/by-name/{summonerName}")
    fun getBySummonerName(@Path("summonerName") summonerName: String): Single<SummonerDto>
}