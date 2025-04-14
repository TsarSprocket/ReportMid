package com.tsarsprocket.reportmid.summonerImpl.retrofit.account

import com.tsarsprocket.reportmid.lolServicesApi.riotapi.CallByType
import com.tsarsprocket.reportmid.lolServicesApi.riotapi.ServerInfo
import retrofit2.http.GET
import retrofit2.http.Path

@ServerInfo(callBy = CallByType.SUPER_REGION)
interface AccountV1Service {

    @GET("$BASE_PATH/by-riot-id/{$GAME_NAME}/{$TAG_LINE}")
    suspend fun getByRiotId(@Path(GAME_NAME) gameName: String, @Path(TAG_LINE) tagLine: String): AccountDto

    @GET("$BASE_PATH/by-puuid/{$PUUID}")
    suspend fun getByPuuid(@Path(PUUID) puuid: String): AccountDto

    private companion object {
        const val BASE_PATH = "/riot/account/v1/accounts"
        const val GAME_NAME = "gameName"
        const val PUUID = "puuid"
        const val TAG_LINE = "tagLine"
    }
}