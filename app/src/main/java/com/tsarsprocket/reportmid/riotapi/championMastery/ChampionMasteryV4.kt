package com.tsarsprocket.reportmid.riotapi.championMastery

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ChampionMasteryV4 {

    @GET("lol/champion-mastery/v4/champion-masteries/by-summoner/{summonerId}")
    fun getBySummonerId(@Path("summonerId") summonerId: String): Single<List<ChampionMasteryDto>>

    @GET("lol/champion-mastery/v4/champion-masteries/by-puuid/{puuid}")
    fun getByPuuid(@Path("puuid") puuid: String): Single<List<ChampionMasteryDto>>

    @GET("lol/champion-mastery/v4/champion-masteries/by-summoner/{summonerId}/by-champion/{championId}")
    fun getBySummonerIdAndChampionId(
        @Path("summonerId") summonerId: String,
        @Path("championId") championId: String
    ): Single<ChampionMasteryDto>

    @GET("lol/champion-mastery/v4/scores/by-summoner/{summonerId}")
    fun getScoresBySummonerId(@Path("summonerId") summonerId: String): Single<Int>
}