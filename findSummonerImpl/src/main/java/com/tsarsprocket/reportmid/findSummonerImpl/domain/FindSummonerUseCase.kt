package com.tsarsprocket.reportmid.findSummonerImpl.domain

import com.tsarsprocket.reportmid.lol.api.model.Region

internal interface FindSummonerUseCase {
    suspend fun findAccount(gameName: String, tagline: String, region: Region): AccountData
    suspend fun getSummonerData(accountData: AccountData): SummonerData
}
