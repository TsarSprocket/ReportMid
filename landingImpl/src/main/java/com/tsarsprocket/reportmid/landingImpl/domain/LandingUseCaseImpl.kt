package com.tsarsprocket.reportmid.landingImpl.domain

import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Io
import com.tsarsprocket.reportmid.dataDragonApi.data.DataDragon
import com.tsarsprocket.reportmid.stateApi.data.StateRepository
import com.tsarsprocket.reportmid.summonerApi.data.SummonerRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class LandingUseCaseImpl @Inject constructor(
    private val stateRepository: StateRepository,
    private val summonerRepository: SummonerRepository,
    private val dataDragon: DataDragon,
    @Io private val ioDispatcher: CoroutineDispatcher,
) : LandingUseCase {

    override suspend fun checkAccountExists(): Boolean = withContext(ioDispatcher) {
        if(stateRepository.getActiveCurrentAccountId() == null) {
            summonerRepository.getAllMyAccounts().firstOrNull()?.let { myAnyAccount ->
                stateRepository.setActiveCurrentAccountId(myAnyAccount.id)
                true
            } ?: false
        } else {
            true
        }
    }

    override suspend fun initializeDataDragon() = withContext(ioDispatcher) {
        dataDragon.initialize()
    }
}