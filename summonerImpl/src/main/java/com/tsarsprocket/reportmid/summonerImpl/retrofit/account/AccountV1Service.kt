package com.tsarsprocket.reportmid.summonerImpl.retrofit.account

import com.tsarsprocket.reportmid.lolServicesApi.riotapi.CallByType
import com.tsarsprocket.reportmid.lolServicesApi.riotapi.ServerInfo
import retrofit2.http.GET
import retrofit2.http.Path

@ServerInfo(callBy = CallByType.SUPER_REGION)
interface AccountV1Service {

    @GET("/riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}")
    suspend fun getByRiotId(@Path("gameName") gameName: String, @Path("tagLine") tagLine: String): AccountDto
}