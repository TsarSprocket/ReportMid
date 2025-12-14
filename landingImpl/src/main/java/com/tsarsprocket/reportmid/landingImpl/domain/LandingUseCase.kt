package com.tsarsprocket.reportmid.landingImpl.domain

import com.tsarsprocket.reportmid.lol.api.domain.model.Puuid
import com.tsarsprocket.reportmid.lol.api.domain.model.PuuidAndRegion
import com.tsarsprocket.reportmid.lol.api.domain.model.Region

internal interface LandingUseCase {
    suspend fun getExistingAccountPuuidAndRegion(): PuuidAndRegion?
    suspend fun createAccount(puuid: Puuid, region: Region)
    suspend fun initializeDataDragon()
}