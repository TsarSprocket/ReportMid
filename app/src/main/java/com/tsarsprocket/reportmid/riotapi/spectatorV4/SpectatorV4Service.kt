package com.tsarsprocket.reportmid.riotapi.spectatorV4

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface SpectatorV4Service {
    @GET("lol/spectator/v4/active-games/by-summoner/{encryptedSummonerId}")
    fun spectatorV4(@Path("encryptedSummonerId") encryptedSummonerId: String): Observable<CurrentGameInfo>
}