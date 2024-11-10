package com.tsarsprocket.reportmid.findSummonerImpl.domain

import com.tsarsprocket.reportmid.lol.model.GameName
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.lol.model.TagLine

internal interface FindSummonerUseCase {
    suspend fun findAccount(gameName: GameName, tagline: TagLine, region: Region): AccountData
    suspend fun getSummonerData(accountData: AccountData): SummonerData
}
