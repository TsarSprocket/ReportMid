package com.tsarsprocket.reportmid.landingImpl.domain

import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Io
import com.tsarsprocket.reportmid.dataDragonApi.data.DataDragon
import com.tsarsprocket.reportmid.lol.model.Puuid
import com.tsarsprocket.reportmid.lol.model.PuuidAndRegion
import com.tsarsprocket.reportmid.lol.model.Region
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

    override suspend fun getExistingAccountPuuidAndRegion(): PuuidAndRegion? = withContext(ioDispatcher) {
        stateRepository.getActiveCurrentAccountId()?.let {
            val currentAccount = stateRepository.getCurrentAccountById(it)
            val myAccount = summonerRepository.getMyAccountById(currentAccount.myAccountId)
            val summoner = summonerRepository.getSummonerInfoById(myAccount.summonerId)
            PuuidAndRegion(summoner.puuid, summoner.region)
        } ?: summonerRepository.getAllMyAccounts().firstOrNull()?.let { myAnyAccount ->
            stateRepository.setActiveCurrentAccountId(myAnyAccount.id)
            summonerRepository.getSummonerInfoById(myAnyAccount.summonerId).let {
                PuuidAndRegion(it.puuid, it.region)
            }
        }
    }

    override suspend fun createAccount(puuid: Puuid, region: Region) {
        val myAccount = summonerRepository.createMyAccount(puuid, region)
        val currentAccount = stateRepository.setCurrentAccountIdByRegion(region, myAccount.id)
        stateRepository.setActiveCurrentAccountId(currentAccount.id)
    }

    override suspend fun initializeDataDragon() = withContext(ioDispatcher) {
        dataDragon.initialize()
    }
}