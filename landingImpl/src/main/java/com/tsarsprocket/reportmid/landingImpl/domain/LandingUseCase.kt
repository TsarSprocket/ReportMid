package com.tsarsprocket.reportmid.landingImpl.domain

internal interface LandingUseCase {
    suspend fun checkAccountExists(): Boolean
    suspend fun initializeDataDragon()
}