package com.tsarsprocket.reportmid.findSummonerImpl.domain

import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Io
import com.tsarsprocket.reportmid.dataDragonApi.data.DataDragon
import com.tsarsprocket.reportmid.lol.api.model.PuuidAndRegion
import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.summonerApi.data.SummonerRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class FindSummonerUseCaseImpl @Inject constructor(
    private val summonerRepository: SummonerRepository,
    private val dataDragon: DataDragon,
    @Io private val ioDispatcher: CoroutineDispatcher,
) : FindSummonerUseCase {

    override suspend fun findAccount(gameName: String, tagline: String, region: Region): AccountData = withContext(ioDispatcher) {
        try {
            summonerRepository.getRiotAccountByGameName(gameName, tagline, region).run {
                AccountData(
                    gameName = gameName,
                    tagline = tagline,
                    puuid = puuid,
                    region = region,
                    isAlreadyInUse = summonerRepository.isSummonerKnown(PuuidAndRegion(puuid, region))
                )
            }
        } catch(exception: Exception) {
            throw AccountNotFoundException()
        }
    }

    override suspend fun getSummonerData(accountData: AccountData): SummonerData = withContext(ioDispatcher) {
        summonerRepository.requestRemoteSummonerByPuuidAndRegion(PuuidAndRegion(accountData.puuid, accountData.region)).run {
            SummonerData(
                puuid = puuid.value,
                region = region,
                gameName = accountData.gameName,
                tagLine = accountData.tagline,
                iconUrl = dataDragon.tail.getSummonerImageUrl(iconId),
                level = level,
            )
        }
    }
}