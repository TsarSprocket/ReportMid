package com.tsarsprocket.reportmid.landingImpl.domain

import com.tsarsprocket.reportmid.lol.model.Puuid
import com.tsarsprocket.reportmid.lol.model.Region

internal interface LandingUseCase {
    suspend fun checkAccountExists(): Boolean
    suspend fun createAccount(puuid: Puuid, region: Region)
    suspend fun initializeDataDragon()
}