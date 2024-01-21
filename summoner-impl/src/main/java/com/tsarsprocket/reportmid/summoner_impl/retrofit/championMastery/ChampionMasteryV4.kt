package com.tsarsprocket.reportmid.summoner_impl.retrofit.championMastery

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ChampionMasteryV4 {

    @GET("lol/champion-mastery/v4/champion-masteries/by-puuid/{puuid}")
    fun getByPuuid(@Path("puuid") puuid: String): Single<List<ChampionMasteryDto>>
}