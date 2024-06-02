package com.tsarsprocket.reportmid.dataDragonImpl.retrofit

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface DataDragonService {
    @GET("api/versions.json")
    fun versions(): Observable<List<String>>

    @GET("cdn/languages.json")
    suspend fun languages(): List<String>

    @GET("cdn/{version}/data/{lang}/runesReforged.json")
    fun runesReforged(@Path("version") version: String, @Path("lang") lang: String): Observable<List<RunePathDto>>

    @GET("cdn/{version}/data/{lang}/champion.json")
    fun champions(@Path("version") version: String, @Path("lang")lang: String): Observable<AllChampionsDto>

    @GET("cdn/{version}/data/{lang}/summoner.json")
    fun summonerSpells(@Path("version") version: String, @Path("lang") lang: String): Observable<SummonerSpellsDto>

    @GET("cdn/{version}/data/{lang}/item.json")
    fun items(@Path("version") version: String, @Path("lang") lang: String): Observable<ItemsDto>
}
