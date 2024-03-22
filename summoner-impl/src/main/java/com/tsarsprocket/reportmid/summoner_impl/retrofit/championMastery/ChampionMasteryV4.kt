package com.tsarsprocket.reportmid.summoner_impl.retrofit.championMastery

import retrofit2.http.GET
import retrofit2.http.Path

interface ChampionMasteryV4 {

    @GET("lol/champion-mastery/v4/champion-masteries/by-puuid/{puuid}")
    suspend fun getByPuuid(@Path("puuid") puuid: String): List<ChampionMasteryDto>
}